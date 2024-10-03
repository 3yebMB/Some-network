package dev.m13d.somenet.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.m13d.somenet.domain.friends.FriendsRepository
import dev.m13d.somenet.friends.states.FriendsState

class FriendsViewModel {
    private val _friendsState = MutableLiveData<FriendsState>()
    val friendsState: LiveData<FriendsState> = _friendsState

    fun loadFriends(userId: String) {
        val friendsState = FriendsRepository().loadFriendsFor(userId)
        _friendsState.value = friendsState
    }
}
