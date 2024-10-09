package dev.m13d.somenet.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.m13d.somenet.R
import dev.m13d.somenet.app.CoroutineDispatchers
import dev.m13d.somenet.domain.friends.FriendsRepository
import dev.m13d.somenet.friends.states.FollowState
import dev.m13d.somenet.friends.states.FriendsScreenState
import dev.m13d.somenet.friends.states.FriendsState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendsViewModel(
    private val friendsRepository: FriendsRepository,
    private val dispatchers: CoroutineDispatchers,
    val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private companion object {
        private const val SCREEN_STATE_KEY = "friendsScreenState"
    }

    val screenState: LiveData<FriendsScreenState> = savedStateHandle.getLiveData(SCREEN_STATE_KEY)

    fun loadFriends(userId: String) {
        viewModelScope.launch {
            updateScreenState(FriendsState.Loading)
            val result = withContext(dispatchers.background) {
                friendsRepository.loadFriendsFor(userId)
            }
            updateScreenState(result)
        }
    }

    fun toggleFollowing(userId: String, followeeId: String) {
        viewModelScope.launch {
            updateListOfFriendships(followeeId)
            val updateFollowing = withContext(dispatchers.background) {
                friendsRepository.updateFollowing(userId, followeeId)
            }
            when (updateFollowing) {
                is FollowState.Followed ->
                    updateFollowingState(updateFollowing.following.followedId, true)

                is FollowState.Unfollowed ->
                    updateFollowingState(updateFollowing.following.followedId, false)

                is FollowState.BackendError ->
                    errorUpdatingFollowing(followeeId, R.string.errorFollowingFriend)

                is FollowState.Offline ->
                    errorUpdatingFollowing(followeeId, R.string.offlineError)
            }
        }
    }

    private fun errorUpdatingFollowing(followeeId: String, errorResource: Int) {
        val currentState = savedStateHandle[SCREEN_STATE_KEY] ?: FriendsScreenState()
        val newState = currentState.copy(
            error = errorResource,
            currentlyUpdatingFriends = currentState.currentlyUpdatingFriends - listOf(followeeId)
        )
        savedStateHandle[SCREEN_STATE_KEY] = newState
    }

    private fun updateListOfFriendships(followeeId: String) {
        val currentState = savedStateHandle[SCREEN_STATE_KEY] ?: FriendsScreenState()
        val updateList = currentState.currentlyUpdatingFriends + listOf(followeeId)
        savedStateHandle[SCREEN_STATE_KEY] =
            currentState.copy(currentlyUpdatingFriends = updateList)
    }

    private fun updateFollowingState(followedId: String, isFollowee: Boolean) {
        val currentState = savedStateHandle[SCREEN_STATE_KEY] ?: FriendsScreenState()
        val index = currentState.friends.indexOfFirst { it.user.id == followedId }
        val matchingUser = currentState.friends[index]
        val updatedFriends = currentState.friends.toMutableList().apply {
            set(index, matchingUser.copy(isFollowee = isFollowee))
        }
        val updatedToggles = currentState.currentlyUpdatingFriends - listOf(followedId)
        val updatedState =
            currentState.copy(friends = updatedFriends, currentlyUpdatingFriends = updatedToggles)
        savedStateHandle[SCREEN_STATE_KEY] = updatedState
    }

    private fun updateScreenState(friendsState: FriendsState) {
        val currentState = savedStateHandle[SCREEN_STATE_KEY] ?: FriendsScreenState()
        val newState = when (friendsState) {
            is FriendsState.Loading ->
                currentState.copy(isLoading = true)

            is FriendsState.Loaded ->
                currentState.copy(isLoading = false, friends = friendsState.friends)

            is FriendsState.BackendError ->
                currentState.copy(isLoading = false, error = R.string.fetchingFriendsError)

            is FriendsState.Offline ->
                currentState.copy(isLoading = false, error = R.string.offlineError)
        }
        savedStateHandle[SCREEN_STATE_KEY] = newState
    }
}
