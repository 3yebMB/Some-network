package dev.m13d.somenet.friends

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.app.TestDispatchers
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

    private val anna = User("annaId", "", "")
    private val sara = User("saraId", "", "")
    private val tom = User("tomId", "", "")
    private val friendAnna = Friend(anna, isFollower = true)
    private val friendSara = Friend(sara, isFollower = false)
    private val friendTom = Friend(tom, isFollower = false)
    private val friendsCatalog = InMemoryFriendsCatalog(
        mapOf(
            "jerryId" to listOf(friendTom),
            "lucyId" to listOf(friendAnna, friendSara, friendTom),
            "samId" to emptyList(),
        )
    )

    @Test
    fun noFriendsExisted() {
        val viewModel = FriendsViewModel(
            FriendsRepository(friendsCatalog),
            TestDispatchers()
        )

        viewModel.loadFriends("samId")

        assertEquals(FriendsState.Loaded(emptyList()), viewModel.friendsState.value)
    }

    @Test
    fun loadedSinglePerson() {
        val viewModel = FriendsViewModel(
            FriendsRepository(friendsCatalog),
            TestDispatchers()
        )

        viewModel.loadFriends("jerryId")

        assertEquals(FriendsState.Loaded(listOf(friendTom)), viewModel.friendsState.value)
    }

    @Test
    fun loadedMultipleFriends() {
        val friends = listOf(friendAnna, friendSara, friendTom)
        val viewModel = FriendsViewModel(
            FriendsRepository(friendsCatalog),
            TestDispatchers()
        )

        viewModel.loadFriends("lucyId")

        assertEquals(FriendsState.Loaded(friends), viewModel.friendsState.value)
    }
}
