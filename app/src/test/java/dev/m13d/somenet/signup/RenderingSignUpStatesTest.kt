package dev.m13d.somenet.signup

import dev.m13d.somenet.InstantTaskExecutorExtension
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
class RenderingSignUpStatesTest {

    private val userRepository = UserRepository(InMemoryUserCatalog())
    private val viewModel = SignUpViewModel(RegexCredentialValidator(), userRepository)
    private val tom = User("tomId", "tom@somenet.dev", "about Tom")

    @Test
    fun uiStatesAreDeliveredInParticularOrder() {
        val deliveredStates = mutableListOf<SignUpState>()
        viewModel.signUpState.observeForever { deliveredStates.add(it) }
        viewModel.createAccount(tom.email, "p@S\$w0rd", tom.about)
        assertEquals(
            listOf(SignUpState.Loading, SignUpState.SignedUp(tom)),
            deliveredStates
        )
    }
}
