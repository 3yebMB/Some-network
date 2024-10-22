package dev.m13d.somenet.friends

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dev.m13d.somenet.MainActivity
import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.domain.friends.ToggleFollowing
import dev.m13d.somenet.domain.user.ControllableUserCatalog
import dev.m13d.somenet.domain.user.Following
import dev.m13d.somenet.domain.user.Friend
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.domain.user.UserCatalog
import kotlinx.coroutines.delay
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class FriendsScreenTest {

    @get:Rule
    val testRule = createAndroidComposeRule<MainActivity>()

    private val tom = User("tomId", "tom@somenet.dev", "Something about Tom")
    private val jerry = User("jerryId", "jerry@somenet.dev", "")
    private val users = mutableMapOf("" to mutableListOf(tom, jerry))
    private val friendTom = Friend(tom, isFollowee = false)
    private val friendJerry = Friend(jerry, isFollowee = false)

    @Test
    fun showEmptyFriendsMessage() {
        launchFriends(testRule) {
            //no operations
        } verify {
            emptyFriendsMessageIsDisplayed()
        }
    }

    @Test
    fun showLoadingIndicator() {
        val friendsLoad: suspend () -> List<Friend> = {
            delay(3500L)
            listOf(friendTom, friendJerry)
        }
        replaceUserCatalogWith(ControllableUserCatalog(friendsLoad = friendsLoad))

        launchFriends(testRule) {
            //no operations
        } verify {
            loadingIndicatorIsShown()
        }
    }

    @Test
    fun showAvailableFriends() {
        replaceUserCatalogWith(InMemoryUserCatalog(users))

        launchFriends(testRule) {
            //no operations
        } verify {
            friendsAreDisplayed(friendTom, friendJerry)
        }
    }

    @Test
    fun showRequiredFriendsInformation() {
        val users = mutableMapOf("" to mutableListOf(tom))
        replaceUserCatalogWith(InMemoryUserCatalog(users))

        launchFriends(testRule) {
            //no operations
        } verify {
            friendInformationIsDisplayedFor(friendTom)
        }
    }

    @Test
    fun showBackendError() {
        val friendsLoad: suspend () -> List<Friend> = { throw BackendException() }
        replaceUserCatalogWith(ControllableUserCatalog(friendsLoad = friendsLoad))

        launchFriends(testRule) {
            //no operations
        } verify {
            backendErrorIsDisplayed()
        }
    }

    @Test
    fun showOfflineError() {
        val friendsLoad: suspend () -> List<Friend> = { throw ConnectionUnavailableException() }
        replaceUserCatalogWith(ControllableUserCatalog(friendsLoad = friendsLoad))

        launchFriends(testRule) {
            //no operations
        } verify {
            offlineErrorIsDisplayed()
        }
    }

    @Test
    fun followAFriend() {
        replaceUserCatalogWith(InMemoryUserCatalog(users))

        launchFriends(testRule) {
            tapOnFollowFor(friendTom)
        } verify {
            followingIsAddedFor(friendTom)
        }
    }

    @Test
    fun unfollowAFriend() {
        replaceUserCatalogWith(InMemoryUserCatalog(users))

        launchFriends(testRule) {
            tapOnFollowFor(friendJerry)
            tapOnUnfollowFor(friendJerry)
        } verify {
            followingIsRemovedFor(friendJerry)
        }
    }

    @Test
    fun showLoadingIndicatorWhileTogglingFriendship() {
        val friendsLoad: suspend () -> List<Friend> = { listOf(friendTom, friendJerry) }
        val toggleFollow: suspend (String, String) -> ToggleFollowing = { userId, followingId ->
            delay(3500L)
            ToggleFollowing(Following(userId, followingId), true)
        }
        replaceUserCatalogWith(ControllableUserCatalog(friendsLoad = friendsLoad, followToggle = toggleFollow))

        launchFriends(testRule) {
            tapOnFollowFor(friendJerry)
        } verify {
            loadingIndicatorIsShownWhileTogglingFriendshipFor(friendJerry)
        }
    }

    @Test
    fun showBackendErrorWhenUpdatingFollowing() {
        val friendsLoad: suspend () -> List<Friend> = { listOf(friendTom, friendJerry) }
        val toggleFollow: suspend (String, String) -> ToggleFollowing = { _, _ ->
            throw BackendException()
        }
        replaceUserCatalogWith(ControllableUserCatalog(friendsLoad = friendsLoad, followToggle = toggleFollow))

        launchFriends(testRule) {
            tapOnFollowFor(friendJerry)
        } verify {
            backendErrorFollowingFriendIsDisplayed()
        }
    }

    @After
    fun tearDown() {
        replaceUserCatalogWith(InMemoryUserCatalog())
    }

    private fun replaceUserCatalogWith(userCatalog: UserCatalog) {
        val replaceModule = module {
            factory { userCatalog }
        }
        loadKoinModules(replaceModule)
    }
}
