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

    @Test
    fun backendError() {
        val viewModel = FriendsViewModel(
            FriendsRepository(
                InMemoryFriendsCatalog(
                    mapOf(
                        "jerryId" to listOf(
                            Friend(
                                User("tomId", ":email:", ":about:"),
                                isFollow = false
                            )
                        ),
                        "lucyId" to listOf(
                            Friend(User("annaId", "", ""), isFollow = true),
                            Friend(User("saraId", "", ""), isFollow = false),
                            Friend(User("tomId", ":email:", ":about:"), isFollow = false)
                        ),
                        "samId" to emptyList(),
                    )
                )
            )
        )

        viewModel.loadFriends("mihalyId")

        assertEquals(FriendsState.BackendError, viewModel.friendsState.value)
    }

    @Test
    fun offlineError() {
        val viewModel = FriendsViewModel(
            FriendsRepository(
                InMemoryFriendsCatalog(
                    mapOf(
                        "jerryId" to listOf(
                            Friend(
                                User("tomId", ":email:", ":about:"),
                                isFollow = false
                            )
                        ),
                        "lucyId" to listOf(
                            Friend(User("annaId", "", ""), isFollow = true),
                            Friend(User("saraId", "", ""), isFollow = false),
                            Friend(User("tomId", ":email:", ":about:"), isFollow = false)
                        ),
                        "samId" to emptyList(),
                    )
                )
            )
        )

        viewModel.loadFriends("jovId")

        assertEquals(FriendsState.Offline, viewModel.friendsState.value)
    }
}
