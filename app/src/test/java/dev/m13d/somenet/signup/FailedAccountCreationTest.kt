package dev.m13d.somenet.signup

import dev.m13d.somenet.domain.user.OfflineUserCatalog
import dev.m13d.somenet.domain.user.UnavailableUserCatalog
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
}
