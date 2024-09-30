package dev.m13d.somenet.domain.user

class InMemoryUserDataStore(
    private var loggedInUserId: String = "",
) {

    fun loggedInUserId() = loggedInUserId

    fun storeLoggedInUserId(userId: String) {
        loggedInUserId = userId
    }
}
