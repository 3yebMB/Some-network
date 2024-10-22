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
    private val savedStateHandle: SavedStateHandle,
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
                is FollowState.Followed -> setFollowee(updateFollowing.following.followedId)
                is FollowState.Unfollowed -> removeFollowee(updateFollowing.following.followedId)
                is FollowState.BackendError -> errorUpdatingFollowing(followeeId, R.string.errorFollowingFriend)
                is FollowState.Offline -> errorUpdatingFollowing(followeeId, R.string.offlineError)
            }
        }
    }

    private fun updateScreenState(friendsState: FriendsState) {
        val currentState = currentScreenState()
        val newState = when (friendsState) {
            is FriendsState.Loading -> currentState.copy(isLoading = true)
            is FriendsState.Loaded -> currentState.copy(isLoading = false, friends = friendsState.friends)
            is FriendsState.BackendError -> currentState.copy(isLoading = false, error = R.string.fetchingFriendsError)
            is FriendsState.Offline -> currentState.copy(isLoading = false, error = R.string.offlineError)
        }
        updateScreenState(newState)
    }

    private fun currentScreenState(): FriendsScreenState {
        return savedStateHandle[SCREEN_STATE_KEY] ?: FriendsScreenState()
    }

    private fun updateScreenState(newState: FriendsScreenState) {
        savedStateHandle[SCREEN_STATE_KEY] = newState
    }

    private fun errorUpdatingFollowing(followeeId: String, errorResource: Int) {
        val currentState = currentScreenState()
        val newState = currentState.copy(
            error = errorResource,
            updatingFriends = currentState.updatingFriends - listOf(followeeId).toSet()
        )
        updateScreenState(newState)
    }

    private fun updateListOfFriendships(followeeId: String) {
        val currentState = currentScreenState()
        val updatedList = currentState.updatingFriends + listOf(followeeId)
        updateScreenState(currentState.copy(updatingFriends = updatedList))
    }

    private fun setFollowee(followeeId: String) {
        updateFollowingState(followeeId, true)
    }

    private fun removeFollowee(followeeId: String) {
        updateFollowingState(followeeId, false)
    }

    private fun updateFollowingState(followedId: String, isFollowee: Boolean) {
        val currentState = currentScreenState()
        val index = currentState.friends.indexOfFirst { it.user.id == followedId }
        val matchingUser = currentState.friends[index]
        val updatedFriends = currentState.friends.toMutableList()
            .apply { set(index, matchingUser.copy(isFollowee = isFollowee)) }
        val updatedToggles = currentState.updatingFriends - listOf(followedId).toSet()
        val updatedState = currentState.copy(friends = updatedFriends, updatingFriends = updatedToggles)
        updateScreenState(updatedState)
    }
}
