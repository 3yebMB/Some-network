package dev.m13d.somenet.domain.user

import dev.m13d.somenet.domain.exceptions.DuplicateAccountException

typealias PasswordUserType = MutableMap<String, MutableList<User>>

class InMemoryUserCatalog(
    private val users: PasswordUserType = mutableMapOf(),
    private val followings: List<Following> = mutableListOf(),
) : UserCatalog {

    override suspend fun createUser(
        email: String,
        password: String,
        about: String,
    ): User {
        checkAccountExists(email)
        val userId = createUserId(email)
        val user = User(id = userId, email = email, about = about)
        saveUser(password, user)
        return user
    }

    override suspend fun followedBy(userId: String): List<String> {
        return followings
            .filter { it.userId == userId }
            .map { it.followedId }
    }

    override suspend fun loadFriendsFor(userId: String): List<Friend> {
        val friendsFollowedByUser = followedBy(userId)
        val allUsers = users.values.flatten()
        return allUsers.map { user -> Friend(user, user.id in friendsFollowedByUser) }
    }

    private fun checkAccountExists(email: String) {
        if (users.values.flatten().any { it.email == email })
            throw DuplicateAccountException()
    }

    private fun createUserId(email: String): String {
        return email.takeWhile { it != '@' } + "Id"
    }

    private fun saveUser(password: String, user: User) {
        users.getOrPut(password, ::mutableListOf).add(user)
    }
}
