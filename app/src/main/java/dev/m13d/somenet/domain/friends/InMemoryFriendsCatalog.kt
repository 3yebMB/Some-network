package dev.m13d.somenet.domain.friends

import dev.m13d.somenet.domain.user.Friend

class InMemoryFriendsCatalog(
    private val friendsForUserId: Map<String, List<Friend>>
) : FriendsCatalog {

    override suspend fun loadFriendsFor(userId: String): List<Friend> {
        return friendsForUserId.getValue(userId)
    }
}
