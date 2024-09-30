package dev.m13d.somenet.domain.user

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StoreSignedUpUserDataTest {

    @Test
    fun successSigningUp() = runBlocking {
        val userDataStore = InMemoryUserDataStore()
        val userRepository = UserRepository(InMemoryUserCatalog(), userDataStore)

        userRepository.signUp("user@email.com", ":password:", ":about:")

        assertEquals("userId", userDataStore.loggedInUserId())
    }
}
