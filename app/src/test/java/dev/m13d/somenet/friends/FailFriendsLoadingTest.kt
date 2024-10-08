package dev.m13d.somenet.friends

import androidx.lifecycle.SavedStateHandle
import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.R
import dev.m13d.somenet.app.TestDispatchers
import dev.m13d.somenet.domain.friends.FriendsRepository
import dev.m13d.somenet.domain.user.OfflineUserCatalog
import dev.m13d.somenet.domain.user.UnavailableUserCatalog
import dev.m13d.somenet.friends.states.FriendsScreenState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class FailFriendsLoadingTest {

    private val testDispatchers = TestDispatchers()
    private val savedStateHandle = SavedStateHandle()

    @Test
    fun backendError() {
        val viewModel = FriendsViewModel(
            FriendsRepository(UnavailableUserCatalog()), testDispatchers, savedStateHandle
        )

        viewModel.loadFriends(":irrelevant:")

        assertEquals(FriendsScreenState(error = R.string.fetchingFriendsError), viewModel.screenState.value)
    }

    @Test
    fun offlineError() {
        val viewModel = FriendsViewModel(
            FriendsRepository(OfflineUserCatalog()), testDispatchers, savedStateHandle
        )

        viewModel.loadFriends(":irrelevant:")

        assertEquals(FriendsScreenState(error = R.string.offlineError), viewModel.screenState.value)
    }
}
