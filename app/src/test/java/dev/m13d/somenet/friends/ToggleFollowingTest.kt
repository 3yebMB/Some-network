package dev.m13d.somenet.friends

import androidx.lifecycle.SavedStateHandle
import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.app.TestDispatchers
import dev.m13d.somenet.domain.friends.FriendsRepository
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
class ToggleFollowingTest {

    @Test
    fun follow() {
        val tom = aUser().withId("tomId").build()
        val jerry = aUser().withId("jerryId").build()
        val users = mutableMapOf(":irrelevant:" to mutableListOf(tom, jerry))

        val dispatchers = TestDispatchers()
        val savedStateHandle = SavedStateHandle()

        val repository = FriendsRepository(InMemoryUserCatalog(users))
        val viewModel = FriendsViewModel(repository, dispatchers, savedStateHandle).apply {
            loadFriends(tom.id)
        }

        viewModel.toggleFollowing(tom.id, jerry.id)

        assertEquals(
            FriendsScreenState(friends = listOf(Friend(jerry, isFollowee = true))),
            viewModel.screenState.value
        )
    }
}
