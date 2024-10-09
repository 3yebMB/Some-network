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
class FailUpdatingFollowingTest {

    private val testDispatchers = TestDispatchers()
    private val savedStateHandle = SavedStateHandle()

    @Test
    fun backendError() {
        val friendsRepository = FriendsRepository(UnavailableUserCatalog())
        val viewModel = FriendsViewModel(friendsRepository, testDispatchers, savedStateHandle)

        viewModel.toggleFollowing(":userId:", ":followeeId:")

        assertEquals(
            FriendsScreenState(error = R.string.errorFollowingFriend),
            viewModel.screenState.value
        )
    }

    @Test
    fun offlineError() {
        val friendsRepository = FriendsRepository(OfflineUserCatalog())
        val viewModel = FriendsViewModel(friendsRepository, testDispatchers, savedStateHandle)

        viewModel.toggleFollowing(":userId:", ":followeeId:")

        assertEquals(
            FriendsScreenState(error = R.string.offlineError),
            viewModel.screenState.value
        )
    }

}
