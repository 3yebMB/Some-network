package dev.m13d.somenet.friends

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.domain.friends.FriendsRepository
import dev.m13d.somenet.domain.friends.InMemoryFriendsCatalog
import dev.m13d.somenet.domain.user.Friend
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.friends.states.FriendsState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class FailFriendsLoadingTest {

    private val anna = Friend(User("annaId", "", ""), isFollow = true)
    private val sara = Friend(User("saraId", "", ""), isFollow = false)
    private val tom = Friend(User("tomId", "", ""), isFollow = false)
    private val friendsCatalog = InMemoryFriendsCatalog(
        mapOf(
            "jerryId" to listOf(tom),
            "lucyId" to listOf(anna, sara, tom),
            "samId" to emptyList(),
        )
    )

    @Test
    fun backendError() {
        val viewModel = FriendsViewModel(FriendsRepository(friendsCatalog))

        viewModel.loadFriends("mihalyId")

        assertEquals(FriendsState.BackendError, viewModel.friendsState.value)
    }

    @Test
    fun offlineError() {
        val viewModel = FriendsViewModel(FriendsRepository(friendsCatalog))

        viewModel.loadFriends("jovId")

        assertEquals(FriendsState.Offline, viewModel.friendsState.value)
    }
}
