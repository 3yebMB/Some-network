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

    @Test
    fun noFriendsExisted() {
        val viewModel = FriendsViewModel(FriendsRepository(InMemoryFriendsCatalog(
            mapOf(
                "jerryId" to listOf(Friend(User("tomId", ":email:", ":about:"), isFollow = false)),
                "lucyId" to listOf(
                    Friend(User("annaId", "", ""), isFollow = true),
                    Friend(User("saraId", "", ""), isFollow = false),
                    Friend(User("tomId", ":email:", ":about:"), isFollow = false)
                ),
                "samId" to emptyList(),
            )
        )
        ))

        viewModel.loadFriends("samId")

        assertEquals(FriendsState.Loaded(emptyList()), viewModel.friendsState.value)
    }

    @Test
    fun loadedSinglePerson() {
        val tom = Friend(User("tomId", ":email:", ":about:"), isFollow = false)
        val viewModel = FriendsViewModel(FriendsRepository(InMemoryFriendsCatalog(
            mapOf(
                "jerryId" to listOf(Friend(User("tomId", ":email:", ":about:"), isFollow = false)),
                "lucyId" to listOf(
                    Friend(User("annaId", "", ""), isFollow = true),
                    Friend(User("saraId", "", ""), isFollow = false),
                    Friend(User("tomId", ":email:", ":about:"), isFollow = false)
                ),
                "samId" to emptyList(),
            )
        )
        ))

        viewModel.loadFriends("jerryId")

        assertEquals(FriendsState.Loaded(listOf(tom)), viewModel.friendsState.value)
    }

    @Test
    fun loadedMultipleFriends() {
        val anna = Friend(User("annaId", "", ""), isFollow = true)
        val sara = Friend(User("saraId", "", ""), isFollow = false)
        val tom = Friend(User("tomId", "", ""), isFollow = false)
        val friends = listOf(anna, sara, tom)
        val viewModel = FriendsViewModel(FriendsRepository(InMemoryFriendsCatalog(
            mapOf(
                "jerryId" to listOf(Friend(User("tomId", ":email:", ":about:"), isFollow = false)),
                "lucyId" to listOf(
                    Friend(User("annaId", "", ""), isFollow = true),
                    Friend(User("saraId", "", ""), isFollow = false),
                    Friend(User("tomId", ":email:", ":about:"), isFollow = false)
                ),
                "samId" to emptyList(),
            )
        )
        ))

        viewModel.loadFriends("lucyId")

        assertEquals(FriendsState.Loaded(friends), viewModel.friendsState.value)
    }
}
