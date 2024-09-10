package dev.m13d.somenet.domain.user

interface UserCatalog {

    suspend fun createUser(
        email: String,
        password: String,
        about: String,
    ): User

    fun followedBy(userId: String): List<String>
}
