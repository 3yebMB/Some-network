package dev.m13d.somenet.domain.friends

import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.domain.user.UserCatalog
import dev.m13d.somenet.friends.states.FriendsState

class FriendsRepository(
    private val userCatalog: UserCatalog,
) {
    suspend fun loadFriendsFor(userId: String): FriendsState {
        return try {
            val friendsForUserId = userCatalog.loadFriendsFor(userId)
            FriendsState.Loaded(friendsForUserId)
        } catch (backendException: BackendException) {
            FriendsState.BackendError
        } catch (offlineException: ConnectionUnavailableException) {
            FriendsState.Offline
        }
    }
}
