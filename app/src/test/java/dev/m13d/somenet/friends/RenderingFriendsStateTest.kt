package dev.m13d.somenet.friends

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.app.TestDispatchers
import dev.m13d.somenet.domain.friends.FriendsRepository
import dev.m13d.somenet.domain.friends.InMemoryFriendsCatalog
import dev.m13d.somenet.domain.user.Friend
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.friends.states.FriendsState
import dev.m13d.somenet.infrastructure.builder.UserBuilder.Companion.aUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi

@ExperimentalUuidApi
@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class RenderingFriendsStateTest {

    private val tom = Friend(aUser().withId("tomId").build(), isFollow = true)
    private val anna = Friend(aUser().withId("annaId").build(), isFollow = true)
    private val friendsCatalog = InMemoryFriendsCatalog(
        mapOf(
            "mihalyId" to listOf(tom, anna)
        )
    )

    @Test
    fun friendsStatesExposedToObserver() {
        val viewModel = FriendsViewModel(FriendsRepository(friendsCatalog), TestDispatchers())
        val deliveredState = mutableListOf<FriendsState>()

        viewModel.friendsState.observeForever { deliveredState.add(it) }
        viewModel.loadFriends("mihalyId")

        assertEquals(
            listOf(FriendsState.Loading, FriendsState.Loaded(listOf(tom, anna))),
            deliveredState
        )
    }
}
