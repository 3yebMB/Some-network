package dev.m13d.somenet.domain.user

import dev.m13d.somenet.domain.friends.ToggleFollowing
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
        private val desiredUserId: String,
    ) : UserCatalog {

        override suspend fun createUser(email: String, password: String, about: String): User {
            return User(desiredUserId, email, about)
        }

        override suspend fun followedBy(userId: String): List<String> {
            TODO("Not yet implemented")
        }

        override suspend fun loadFriendsFor(userId: String): List<Friend> {
            TODO("Not yet implemented")
        }

        override suspend fun toggleFollowing(userId: String, followeeId: String): ToggleFollowing {
            TODO("Not yet implemented")
        }
    }
}
