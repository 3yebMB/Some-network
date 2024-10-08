package dev.m13d.somenet.infrastructure.builder

import dev.m13d.somenet.domain.user.User
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@ExperimentalUuidApi
class UserBuilder {
    private var userId = Uuid.random().toString()

    private var userEmail = "user@somenet.dev"
    private var aboutUser = "About User"

    companion object {
        fun aUser(): UserBuilder {
            return UserBuilder()
        }
    }

    fun withId(id: String): UserBuilder = this.apply {
        userId = id
    }

    fun build(): User {
        return User(userId, userEmail, aboutUser)
    }
}
