package dev.m13d.somenet.domain.user

class InMemoryUserDataStore(
    private val loggedInUserId: String,
) {

    fun loggedInUserId() = loggedInUserId
}
