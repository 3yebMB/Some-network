package dev.m13d.somenet.domain.friends

import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.domain.user.Friend

class InMemoryFriendsCatalog(
    private val friendsForUserId: Map<String, List<Friend>>
) : FriendsCatalog {

    override fun loadFriendsFor(userId: String): List<Friend> {
        when(userId) {
            "mihalyId" -> throw BackendException()
            "jovId" -> throw ConnectionUnavailableException()
        }
        return friendsForUserId.getValue(userId)
    }
}
