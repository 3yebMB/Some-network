package dev.m13d.somenet.signup

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.domain.user.UserRepository
import dev.m13d.somenet.domain.validation.RegexCredentialValidator
import dev.m13d.somenet.signup.states.SignUpState
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(InstantTaskExecutorExtension::class)
class RenderingSignUpStatesTest {

    @Test
    fun uiStatesAreDeliveredInParticularOrder() {

        val userRepository = UserRepository(InMemoryUserCatalog())
        val viewModel = SignUpViewModel(RegexCredentialValidator(), userRepository)
        val tom = User("tomId", "tom@somenet.dev", "about Tom")
        val deliveredStates = mutableListOf<SignUpState>()
        viewModel.signUpState.observeForever { deliveredStates.add(it) }
        viewModel.createAccount(tom.email, "p@S\$w0rd", tom.about)
        assertEquals(listOf(SignUpState.Loading, SignUpState.SignedUp(tom)), deliveredStates)
    }
}
