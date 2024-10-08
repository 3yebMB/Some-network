package dev.m13d.somenet.domain.user

import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.friends.ToggleFollowing

class UnavailableUserCatalog : UserCatalog {

    override suspend fun createUser(email: String, password: String, about: String): User {
        throw BackendException()
    }

    override suspend fun followedBy(userId: String): List<String> {
        throw BackendException()
    }

    override suspend fun loadFriendsFor(userId: String): List<Friend> {
        throw BackendException()
    }

    override fun toggleFollowing(userId: String, followeeId: String): ToggleFollowing {
        throw BackendException()
    }
}
