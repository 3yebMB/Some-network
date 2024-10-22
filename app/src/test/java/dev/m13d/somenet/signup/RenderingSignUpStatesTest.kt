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

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class RenderingSignUpStatesTest {

    private val userRepository = UserRepository(InMemoryUserCatalog(), InMemoryUserDataStore())
    private val viewModel = SignUpViewModel(
        RegexCredentialValidator(),
        userRepository,
        SavedStateHandle(),
        TestDispatchers(),
    )
    private val tom = User("tomId", "tom@somenet.dev", "about Tom")

    @Test
    fun uiStatesAreDeliveredInParticularOrder() {
        val deliveredStates = mutableListOf<SignUpScreenState>()
        viewModel.screenState.observeForever { deliveredStates.add(it) }
        viewModel.createAccount(tom.email, "p@Ssw0rd", tom.about)
        assertEquals(
            listOf(SignUpScreenState(isLoading = true), SignUpScreenState(signedUpUserId = tom.id)),
            deliveredStates
        )
    }
}
