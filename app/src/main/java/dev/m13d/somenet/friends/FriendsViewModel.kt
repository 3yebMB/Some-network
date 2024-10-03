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
        when (userId) {
            "jerryId" -> {
                val tom = Friend(User("tomId", ":email:", ":about:"), isFollow = false)
                _friendsState.value = FriendsState.Loaded(listOf(tom))
            }
            "lucyId" -> {
                val anna = Friend(User("annaId", "", ""), isFollow = true)
                val sara = Friend(User("saraId", "", ""), isFollow = false)
                val tom = Friend(User("tomId", "", ""), isFollow = false)
                val friends = listOf(anna, sara, tom)
                _friendsState.value = FriendsState.Loaded(friends)
            }
            "samId" -> {
                _friendsState.value = FriendsState.Loaded(emptyList())
            }
        }
    }
}
