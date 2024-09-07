package dev.m13d.somenet.domain.user

interface UserCatalog {

    fun createUser(
        email: String,
        password: String,
        about: String,
    ): User
}
