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

    @Test
    fun anotherAccountCreated() {
        val balazs = User("balazsId", "balazs@somenet.dev", "Text about Balazs")
        val viewModel = SignUpViewModel(RegexCredentialValidator())
        viewModel.createAccount(balazs.email, "Bl@-B1a_blA", balazs.about)
        assertEquals(SignUpState.SignedUp(balazs), viewModel.signUpState.value)
    }

    @Test
    fun createExistedAccount() {
        val password = "OrSo1y@+"
        val orsolya = User("OrsolyaId", "orsolya@somenet.dev", "Facts about Orsolya")
        val viewModel = SignUpViewModel(RegexCredentialValidator()).also {
            it.createAccount(orsolya.email, password, orsolya.about)
        }
        viewModel.createAccount(orsolya.email, password, orsolya.about)
        assertEquals(SignUpState.DuplicateAccount, viewModel.signUpState.value)
    }
}
