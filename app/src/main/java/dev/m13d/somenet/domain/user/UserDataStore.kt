package dev.m13d.somenet.domain.user

interface UserDataStore {

    fun loggedInUserId(): String

    fun storeLoggedInUserId(userId: String)
}
