package dev.m13d.somenet.friends

import androidx.compose.ui.util.trace
import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.domain.user.Friend
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.friends.states.FriendsState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi

@ExperimentalUuidApi
@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class LoadFriendsTest {

    @Test
    fun noFriendsExisted() {
        val viewModel = FriendsViewModel()

        viewModel.loadFriends("samId")

        assertEquals(FriendsState.Loaded(emptyList()), viewModel.friendsState.value)
    }

    @Test
    fun loadedSinglePerson() {
        val user = User("tomId", ":email:", ":about:")
        val tom = Friend(user, isFollow = false)
        val viewModel = FriendsViewModel()

        viewModel.loadFriends("jerryId")

        assertEquals(FriendsState.Loaded(listOf(tom)), viewModel.friendsState.value)
    }

    @Test
    fun loadedMultipleFriends() {
        val friendAnna = Friend(User("annaId", "", ""), isFollow = true)
        val friendSara = Friend(User("saraId", "", ""), isFollow = false)
        val friendTom = Friend(User("tomId", "", ""), isFollow = false)
        val friends = listOf(friendAnna, friendSara, friendTom)
        val viewModel = FriendsViewModel()

        viewModel.loadFriends("lucyId")

        assertEquals(FriendsState.Loaded(friends), viewModel.friendsState.value)
    }
}
