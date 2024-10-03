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
import kotlin.uuid.ExperimentalUuidApi

@ExperimentalUuidApi
@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class LoadFriendsTest {

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
    fun noFriendsExisted() {
        val viewModel = FriendsViewModel(FriendsRepository(friendsCatalog))

        viewModel.loadFriends("samId")

        assertEquals(FriendsState.Loaded(emptyList()), viewModel.friendsState.value)
    }

    @Test
    fun loadedSinglePerson() {
        val viewModel = FriendsViewModel(FriendsRepository(friendsCatalog))

        viewModel.loadFriends("jerryId")

        assertEquals(FriendsState.Loaded(listOf(tom)), viewModel.friendsState.value)
    }

    @Test
    fun loadedMultipleFriends() {
        val friends = listOf(anna, sara, tom)
        val viewModel = FriendsViewModel(FriendsRepository(friendsCatalog))

        viewModel.loadFriends("lucyId")

        assertEquals(FriendsState.Loaded(friends), viewModel.friendsState.value)
    }
}
