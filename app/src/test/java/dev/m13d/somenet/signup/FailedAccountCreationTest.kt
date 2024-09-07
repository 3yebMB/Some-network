package dev.m13d.somenet.signup

import android.security.keystore.BackendBusyException
import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.domain.user.UserCatalog
import dev.m13d.somenet.domain.user.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FailedAccountCreationTest {

    @Test
    fun backendError() {
        val userRepository = UserRepository(UnavailableUserCatalog())
        val result = userRepository.signUp("email", "password", "about")
        assertEquals(SignUpState.BackendError, result)
    }
}

class UnavailableUserCatalog : UserCatalog {

    override fun createUser(email: String, password: String, about: String): User {
        throw BackendException()
    }

}
