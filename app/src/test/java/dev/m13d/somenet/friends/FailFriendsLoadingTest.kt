package dev.m13d.somenet.friends

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.domain.friends.FriendsCatalog
import dev.m13d.somenet.domain.friends.FriendsRepository
import dev.m13d.somenet.domain.friends.InMemoryFriendsCatalog
import dev.m13d.somenet.domain.user.Friend
import dev.m13d.somenet.domain.user.UnavailableUserCatalog
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.friends.states.FriendsState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class FailFriendsLoadingTest {

    @Test
    fun backendError() {
        val viewModel = FriendsViewModel(FriendsRepository(UnavailableFriendsCatalog()))

        viewModel.loadFriends(":irrelevant:")

        assertEquals(FriendsState.BackendError, viewModel.friendsState.value)
    }

    @Test
    fun offlineError() {
        val viewModel = FriendsViewModel(FriendsRepository(OfflineFriendsCatalog()))

        viewModel.loadFriends(":irrelevant:")

        assertEquals(FriendsState.Offline, viewModel.friendsState.value)
    }

    private class UnavailableFriendsCatalog : FriendsCatalog {

        override fun loadFriendsFor(userId: String): List<Friend> {
            throw BackendException()
        }
    }

    private class OfflineFriendsCatalog : FriendsCatalog {

        override fun loadFriendsFor(userId: String): List<Friend> {
            throw ConnectionUnavailableException()
        }
    }
}
