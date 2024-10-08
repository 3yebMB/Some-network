package dev.m13d.somenet.friends

import androidx.lifecycle.SavedStateHandle
import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.app.TestDispatchers
import dev.m13d.somenet.domain.friends.FriendsRepository
import dev.m13d.somenet.domain.user.Following
import dev.m13d.somenet.domain.user.Friend
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.friends.states.FriendsScreenState
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

    private val mihaly = aUser().withId("mihalyId").build()
    private val tom = aUser().withId("tomId").build()
    private val anna = aUser().withId("annaId").build()

    private val friendTom = Friend(tom, isFollowee = true)
    private val friendAnna = Friend(anna, isFollowee = true)

    private val userCatalog = InMemoryUserCatalog(
        users = mutableMapOf(":irrelevant:" to mutableListOf(tom, anna)),
        followings = mutableListOf(Following(mihaly.id, tom.id), Following(mihaly.id, anna.id))
    )

    private val viewModel = FriendsViewModel(
        FriendsRepository(userCatalog),
        TestDispatchers(),
        SavedStateHandle(),
    )

    @Test
    fun friendsStatesExposedToObserver() {
        val loading = FriendsScreenState(isLoading = true)
        val loaded = FriendsScreenState(isLoading = false, friends = listOf(friendTom, friendAnna))
        val deliveredState = mutableListOf<FriendsScreenState>()

        viewModel.screenState.observeForever { deliveredState.add(it) }

        viewModel.loadFriends(mihaly.id)

        assertEquals(listOf(loading, loaded), deliveredState)
    }
}
