package dev.m13d.somenet.signup

import androidx.lifecycle.SavedStateHandle
import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.app.TestDispatchers
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.domain.user.InMemoryUserDataStore
import dev.m13d.somenet.domain.user.UserRepository
import dev.m13d.somenet.domain.validation.CredentialsValidationResult
import dev.m13d.somenet.domain.validation.RegexCredentialValidator
import dev.m13d.somenet.signup.states.SignUpScreenState
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
            UserRepository(InMemoryUserCatalog(), InMemoryUserDataStore()),
            SavedStateHandle(),
            TestDispatchers(),
        )
        viewModel.createAccount(email, ":password:", "about")

        assertEquals(SignUpScreenState(isBadEmail = true), viewModel.screenState.value)
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
            UserRepository(InMemoryUserCatalog(), InMemoryUserDataStore()),
            SavedStateHandle(),
            TestDispatchers(),
        )
        viewModel.createAccount("michael@somenet.dev", password, ":about:")

        assertEquals(SignUpScreenState(isBadPassword = true), viewModel.screenState.value)
    }

    @Test
    fun validCredentials() {
        val validator = RegexCredentialValidator()
        val result = validator.validate("johnsmith@somenet.dev", "Q&ZKRmw1")

        assertEquals(CredentialsValidationResult.Valid, result)
    }
}
