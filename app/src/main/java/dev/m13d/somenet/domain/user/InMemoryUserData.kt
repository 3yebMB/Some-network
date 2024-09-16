package dev.m13d.somenet.domain.user

class InMemoryUserData(
    private val loggedInUserId: String,
) {

    fun loggedInUserId() = loggedInUserId
}
