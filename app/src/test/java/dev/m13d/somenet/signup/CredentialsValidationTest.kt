package dev.m13d.somenet.signup

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.app.TestDispatchers
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.domain.user.UserRepository
import dev.m13d.somenet.domain.validation.CredentialsValidationResult
import dev.m13d.somenet.domain.validation.RegexCredentialValidator
import dev.m13d.somenet.signup.states.SignUpState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class CredentialsValidationTest {

    @ParameterizedTest
    @CsvSource(
        "''",
        "' '",
        "'  '",
        "'a@b.c'",
        "'ab@b.c'",
        "'ab@bc.c'",
        "'email'",
    )
    fun invalidEmail(email: String) {
        val viewModel = SignUpViewModel(
            RegexCredentialValidator(),
            UserRepository(InMemoryUserCatalog()),
            TestDispatchers(),
        )
        viewModel.createAccount(email, ":password:", "about")
        assertEquals(SignUpState.BadEmail, viewModel.signUpState.value)
    }

    @ParameterizedTest
    @CsvSource(
        "''",
        "' '",
        "'  '",
        "'12345678'",
        "'abcd5678'",
        "'qwertY'",
        "'123Qwe'",
        "'qwe123#$'",
        "QWERTY123%&",
    )
    fun invalidPassword(password: String) {
        val viewModel = SignUpViewModel(
            RegexCredentialValidator(),
            UserRepository(InMemoryUserCatalog()),
            TestDispatchers(),
        )
        viewModel.createAccount("michael@somenet.dev", password, ":about:")
        assertEquals(SignUpState.BadPassword, viewModel.signUpState.value)
    }

    @Test
    fun validCredentials() {
        val validator = RegexCredentialValidator()
        val result = validator.validate("johnsmith@somenet.dev", "Q&ZKRmw1")
        assertEquals(CredentialsValidationResult.Valid, result)
    }
}
