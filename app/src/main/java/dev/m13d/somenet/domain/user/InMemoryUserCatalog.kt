package dev.m13d.somenet.domain.user

import dev.m13d.somenet.domain.exceptions.DuplicateAccountException

typealias PasswordUserType = MutableMap<String, MutableList<User>>

class InMemoryUserCatalog(
    private val users: PasswordUserType = mutableMapOf()
): UserCatalog {

    override suspend fun createUser(
        email: String,
        password: String,
        about: String
    ): User {
        checkAccountExists(email)
        val userId = createUserId(email)
        val user = User(id = userId, email = email, about = about)
        saveUser(password, user)
        return user
    }

    fun followedBy(userId: String): List<String> {
        val followings = listOf(
            Following("sarahId", "lucyId"),
            Following("anabelId", "lucyId"),
        )
        return followings
            .filter { it.userId == userId }
            .map { it.followedId }
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
