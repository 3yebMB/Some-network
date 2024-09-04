package dev.m13d.somenet.signup

import dev.m13d.somenet.InstantTaskExecutorExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

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
    fun invalidEmail() {
        val viewModel = SignUpViewModel()
        viewModel.createAccount("email", ":password:", "about")
        assertEquals(SignUpState.BadEmail, viewModel.signUpState.value)
    }

    @Test
    fun invalidPassword() {
        val viewModel = SignUpViewModel()
        viewModel.createAccount("michael@somenet.dev", "", ":about:")
        assertEquals(SignUpState.BadPassword, viewModel.signUpState.value)
    }
}
