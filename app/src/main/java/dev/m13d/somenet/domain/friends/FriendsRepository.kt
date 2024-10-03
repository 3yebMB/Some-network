package dev.m13d.somenet.domain.friends

import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.friends.states.FriendsState

class FriendsRepository {
    fun loadFriendsFor(userId: String): FriendsState {
        return try {
            val friendsForUserId = InMemoryFriendsCatalog().loadFriendsFor(userId)
            FriendsState.Loaded(friendsForUserId)
        } catch (backendException: BackendException) {
            FriendsState.BackendError
        } catch (offlineException: ConnectionUnavailableException) {
            FriendsState.Offline
        }
    }
}
