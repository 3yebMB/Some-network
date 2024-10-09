package dev.m13d.somenet.domain.user

import dev.m13d.somenet.domain.friends.ToggleFollowing

class ControllableUserCatalog(
    private val userCreate: suspend (String, String, String) -> User = { email, _, about ->
        val userId = "${email.takeWhile { it == '@' }}Id"
        User(userId, email, about)
    },
    private val followedByLoad: suspend () -> List<String> = { emptyList() },
    private val friendsLoad: suspend () -> List<Friend> = { emptyList() },
    private val followToggle: suspend (String, String) -> ToggleFollowing = { userId, followingId ->
        ToggleFollowing(Following(userId, followingId), true)
    },
) : UserCatalog {

    override suspend fun createUser(email: String, password: String, about: String): User {
        return userCreate(email, password, about)
    }

    override suspend fun followedBy(userId: String): List<String> {
        return followedByLoad()
    }

    override suspend fun loadFriendsFor(userId: String): List<Friend> {
        return friendsLoad()
    }

    override suspend fun toggleFollowing(userId: String, followeeId: String): ToggleFollowing {
        return followToggle(userId, followeeId)
    }
}
