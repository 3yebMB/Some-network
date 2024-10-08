package dev.m13d.somenet.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.m13d.somenet.app.CoroutineDispatchers
import dev.m13d.somenet.domain.friends.FriendsRepository
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
    private val _friendsState = MutableLiveData<FriendsState>()
    val friendsState: LiveData<FriendsState> = _friendsState

    fun loadFriends(userId: String) {
        viewModelScope.launch {
            _friendsState.value = FriendsState.Loading
            _friendsState.value = withContext(dispatchers.background) {
                friendsRepository.loadFriendsFor(userId)
            }
            savedStateHandle[SCREEN_STATE_KEY] = FriendsScreenState()
        }
    }
}
