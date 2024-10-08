package dev.m13d.somenet.domain.user

import dev.m13d.somenet.domain.friends.ToggleFollowing

interface UserCatalog {

    suspend fun createUser(
        email: String,
        password: String,
        about: String,
    ): User

    suspend fun followedBy(userId: String): List<String>

    suspend fun loadFriendsFor(userId: String): List<Friend>

    fun toggleFollowing(userId: String, followeeId: String): ToggleFollowing
}
