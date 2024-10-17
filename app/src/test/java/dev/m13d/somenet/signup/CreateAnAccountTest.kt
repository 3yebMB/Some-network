package dev.m13d.somenet.signup

import androidx.lifecycle.SavedStateHandle
import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.app.TestDispatchers
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.domain.user.InMemoryUserDataStore
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.domain.user.UserRepository
import dev.m13d.somenet.domain.validation.RegexCredentialValidator
import dev.m13d.somenet.signup.states.SignUpScreenState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import dev.m13d.somenet.R

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class CreateAnAccountTest {

    private val credentialValidator = RegexCredentialValidator()
    private val viewModel = SignUpViewModel(
        credentialValidator,
        UserRepository(InMemoryUserCatalog(), InMemoryUserDataStore()),
        SavedStateHandle(),
        TestDispatchers(),
    )

    @Test
    fun accountCreated() {
        val micheal = User("michaelId", "michael@somenet.dev", "Something about Michael")
        viewModel.createAccount(micheal.email, "Mich@e13", micheal.about)
        assertEquals(SignUpScreenState(signedUpUserId = micheal.id), viewModel.screenState.value)
    }

    @Test
    fun anotherAccountCreated() {
        val balazs = User("balazsId", "balazs@somenet.dev", "Text about Balazs")
        viewModel.createAccount(balazs.email, "Bl@-B1a_blA", balazs.about)
        assertEquals(SignUpScreenState(signedUpUserId = balazs.id), viewModel.screenState.value)
    }

    @Test
    fun createExistedAccount() {
        val password = "OrSo1y@+"
        val orsolya = User("OrsolyaId", "orsolya@somenet.dev", "Facts about Orsolya")
        val users = mutableMapOf(password to mutableListOf(orsolya))
        val userRepository = UserRepository(InMemoryUserCatalog(users), InMemoryUserDataStore())
        val viewModel = SignUpViewModel(credentialValidator, userRepository, SavedStateHandle(), TestDispatchers())

        viewModel.createAccount(orsolya.email, password, orsolya.about)
        assertEquals(SignUpScreenState(error = R.string.duplicateAccountError), viewModel.screenState.value)
    }
}
