package dev.m13d.somenet.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.domain.user.Friend
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.friends.states.FriendsState

class FriendsViewModel {
    private val _friendsState = MutableLiveData<FriendsState>()
    val friendsState: LiveData<FriendsState> = _friendsState

    fun loadFriends(userId: String) {
        val friendsState = try {
            val friendsForUserId = InMemoryFriendsCatalog().loadFriendsFor(userId)
            FriendsState.Loaded(friendsForUserId)
        } catch (backendException: BackendException) {
            FriendsState.BackendError
        } catch (offlineException: ConnectionUnavailableException) {
            FriendsState.Offline
        }
        _friendsState.value = friendsState
    }

    class InMemoryFriendsCatalog {
        private val tom = Friend(User("tomId", ":email:", ":about:"), isFollow = false)
        private val anna = Friend(User("annaId", "", ""), isFollow = true)
        private val sara = Friend(User("saraId", "", ""), isFollow = false)
        private val friendsForUserId = mapOf(
            "jerryId" to listOf(tom),
            "lucyId" to listOf(anna, sara, tom),
            "samId" to emptyList(),
        )

        fun loadFriendsFor(userId: String): List<Friend> {
            when(userId) {
                "mihalyId" -> throw BackendException()
                "jovId" -> throw ConnectionUnavailableException()
            }
            return friendsForUserId.getValue(userId)
        }
    }
}
