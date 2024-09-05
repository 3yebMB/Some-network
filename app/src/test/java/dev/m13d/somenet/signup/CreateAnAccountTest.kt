package dev.m13d.somenet.signup

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.domain.validation.RegexCredentialValidator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class CreateAnAccountTest {

    @Test
    fun accountCreated() {
        val micheal = User("michaelId", "michael@somenet.dev", "Something about Michael")
        val viewModel = SignUpViewModel(RegexCredentialValidator())
        viewModel.createAccount(micheal.email, "Mich@e13", micheal.about)
        assertEquals(SignUpState.SignedUp(micheal), viewModel.signUpState.value)
    }
}
