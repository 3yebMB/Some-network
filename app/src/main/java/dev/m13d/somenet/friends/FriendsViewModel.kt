package dev.m13d.somenet.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.m13d.somenet.domain.user.Friend
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.friends.states.FriendsState

class FriendsViewModel {
    private val _friendsState = MutableLiveData<FriendsState>()
    val friendsState: LiveData<FriendsState> = _friendsState
    fun loadFriends(userId: String) {
        if (userId == "jerryId") {
            val tom = Friend(User("tomId", ":email:", ":about:"), isFollow = false)
            _friendsState.value = FriendsState.Loaded(listOf(tom))
        } else {
            _friendsState.value = FriendsState.Loaded(emptyList())
        }
    }
}
