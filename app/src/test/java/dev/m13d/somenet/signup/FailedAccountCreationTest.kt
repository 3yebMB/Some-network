package dev.m13d.somenet.signup

import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.domain.user.UserCatalog
import dev.m13d.somenet.domain.user.UserRepository
import dev.m13d.somenet.signup.states.SignUpState
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FailedAccountCreationTest {

    @Test
    fun backendError() = runBlocking {
        val userRepository = UserRepository(UnavailableUserCatalog())
        val result = userRepository.signUp("email", "password", "about")
        assertEquals(SignUpState.BackendError, result)
    }

    @Test
    fun offlineError() = runBlocking {
        val userRepository = UserRepository(OfflineUserCatalog())
        val result = userRepository.signUp("email", "password", "about")
        assertEquals(SignUpState.Offline, result)
    }

    private class OfflineUserCatalog : UserCatalog {

        override suspend fun createUser(email: String, password: String, about: String): User {
            throw ConnectionUnavailableException()
        }

        override fun followedBy(userId: String): List<String> {
            TODO("Not yet implemented")
        }

    }

    private class UnavailableUserCatalog : UserCatalog {

        override suspend fun createUser(email: String, password: String, about: String): User {
            throw BackendException()
        }

        override fun followedBy(userId: String): List<String> {
            TODO("Not yet implemented")
        }
    }
}
