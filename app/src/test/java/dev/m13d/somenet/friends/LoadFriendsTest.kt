package dev.m13d.somenet.friends

import androidx.lifecycle.SavedStateHandle
import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.app.TestDispatchers
import dev.m13d.somenet.domain.friends.FriendsRepository
import dev.m13d.somenet.domain.user.Following
import dev.m13d.somenet.domain.user.Friend
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.friends.states.FriendsScreenState
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
class LoadFriendsTest {

    private val lucy = aUser().withId("lucyId").build()
    private val tom = aUser().withId("tomId").build()
    private val anna = aUser().withId("annaId").build()
    private val sara = aUser().withId("saraId").build()

    private val friendTom = Friend(tom, isFollower = false)
    private val friendAnna = Friend(anna, isFollower = true)
    private val friendSara = Friend(sara, isFollower = false)

    @Test
    fun noFriendsExisted() {

        val userCatalog = InMemoryUserCatalog()
        val viewModel = FriendsViewModel(
            FriendsRepository(userCatalog),
            TestDispatchers(),
            SavedStateHandle(),
        )

        viewModel.loadFriends(sara.id)

        assertEquals(FriendsState.Loaded(emptyList()), viewModel.friendsState.value)
        assertEquals(FriendsScreenState(), viewModel.screenState.value)
    }

    @Test
    fun loadedSinglePerson() {
        val userCatalog = InMemoryUserCatalog(
            users = mutableMapOf(":irrelevant:" to mutableListOf(tom))
        )
        val viewModel = FriendsViewModel(
            FriendsRepository(userCatalog),
            TestDispatchers(),
            SavedStateHandle(),
        )

        viewModel.loadFriends(anna.id)

        assertEquals(FriendsState.Loaded(listOf(friendTom)), viewModel.friendsState.value)
    }

    @Test
    fun loadedMultipleFriends() {
        val userCatalog = InMemoryUserCatalog(
            users = mutableMapOf(":irrelevant:" to mutableListOf(anna, sara, tom)),
            followings = mutableListOf(Following(lucy.id, anna.id))
        )
        val viewModel = FriendsViewModel(
            FriendsRepository(userCatalog),
            TestDispatchers(),
            SavedStateHandle(),
        )

        viewModel.loadFriends(lucy.id)

        assertEquals(
            FriendsState.Loaded(listOf(friendAnna, friendSara, friendTom)),
            viewModel.friendsState.value
        )
    }

    @Test
    fun loadNoFriendsUsingSignedUpUserId() {
        val userCatalog = InMemoryUserCatalog(
            users = mutableMapOf(":irrelevant:" to mutableListOf(tom))
        )
        val viewModel = FriendsViewModel(
            FriendsRepository(userCatalog),
            TestDispatchers(),
            SavedStateHandle(),
        )

        viewModel.loadFriends(tom.id)

        assertEquals(
            FriendsState.Loaded(emptyList()),
            viewModel.friendsState.value
        )
    }
}
