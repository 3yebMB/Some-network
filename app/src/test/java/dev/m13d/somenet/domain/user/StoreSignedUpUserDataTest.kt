package dev.m13d.somenet.domain.user

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StoreSignedUpUserDataTest {

    @Test
    fun successSigningUp() = runBlocking {
        val userDataStore = InMemoryUserDataStore()
        val userRepository = UserRepository(UserCatalogCreatingUserWith("userId"), userDataStore)

        userRepository.signUp(":email:", ":password:", ":about:")

        assertEquals("userId", userDataStore.loggedInUserId())
    }

    private class UserCatalogCreatingUserWith(
        private val desiredUserId: String
    ) : UserCatalog {

        override suspend fun createUser(email: String, password: String, about: String): User {
            return User(desiredUserId, email, about)
        }

        override fun followedBy(userId: String): List<String> {
            TODO("Not yet implemented")
        }
    }
}