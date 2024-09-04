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
    fun invalidEmail(email: String) {
        val viewModel = SignUpViewModel()
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
        val viewModel = SignUpViewModel()
        viewModel.createAccount("michael@somenet.dev", password, ":about:")
        assertEquals(SignUpState.BadPassword, viewModel.signUpState.value)
    }
}
