package dev.m13d.somenet.signup

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.app.TestDispatchers
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.domain.user.UserRepository
import dev.m13d.somenet.domain.validation.RegexCredentialValidator
import dev.m13d.somenet.signup.states.SignUpState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class CreateAnAccountTest {

    private val credentialValidator = RegexCredentialValidator()
    private val viewModel = SignUpViewModel(
        credentialValidator,
        UserRepository(InMemoryUserCatalog()),
        TestDispatchers(),
    )

    @Test
    fun accountCreated() {
        val micheal = User("michaelId", "michael@somenet.dev", "Something about Michael")
        viewModel.createAccount(micheal.email, "Mich@e13", micheal.about)
        assertEquals(SignUpState.SignedUp(micheal), viewModel.signUpState.value)
    }

    @Test
    fun anotherAccountCreated() {
        val balazs = User("balazsId", "balazs@somenet.dev", "Text about Balazs")
        viewModel.createAccount(balazs.email, "Bl@-B1a_blA", balazs.about)
        assertEquals(SignUpState.SignedUp(balazs), viewModel.signUpState.value)
    }

    @Test
    fun createExistedAccount() {
        val password = "OrSo1y@+"
        val orsolya = User("OrsolyaId", "orsolya@somenet.dev", "Facts about Orsolya")

        val users = mutableMapOf(password to mutableListOf(orsolya))
        val userRepository = UserRepository(InMemoryUserCatalog(users))
        val viewModel = SignUpViewModel(credentialValidator, userRepository, TestDispatchers())

        viewModel.createAccount(orsolya.email, password, orsolya.about)
        assertEquals(SignUpState.DuplicateAccount, viewModel.signUpState.value)
    }
}
