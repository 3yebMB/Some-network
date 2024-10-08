package dev.m13d.somenet.friends

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dev.m13d.somenet.MainActivity
import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.domain.user.ControllableUserCatalog
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
    val rule = createAndroidComposeRule<MainActivity>()

    private val tom = User("tomId", "tom@somenet.dev", "Something about Tom")
    private val jerry = User("jerryId", "jerry@somenet.dev", "")
    private val users = mutableMapOf("" to mutableListOf(tom, jerry))
    private val friendTom = Friend(tom, isFollower = false)
    private val friendJerry = Friend(jerry, isFollower = false)

    @Test
    fun showEmptyFriendsMessage() {
        launchFriends(rule) {
            //no operations
        } verify {
            emptyFriendsMessageIsDisplayed()
        }
    }

    @Test
    fun showLoadingIndicator() {
        val friendsLoad: suspend () -> List<Friend> = {
            delay(1500L)
            listOf(friendTom, friendJerry)
        }
        replaceUserCatalogWith(ControllableUserCatalog(friendsLoad = friendsLoad))

        launchFriends(rule) {
            //no operations
        } verify {
            loadingIndicatorIsShown()
        }
    }

    @Test
    fun showAvailableFriends() {
        replaceUserCatalogWith(InMemoryUserCatalog(users))

        launchFriends(rule) {
            //no operations
        } verify {
            friendsAreDisplayed(friendTom, friendJerry)
        }
    }

    @Test
    fun showRequiredFriendsInformation() {
        val users = mutableMapOf("" to mutableListOf(tom))
        replaceUserCatalogWith(InMemoryUserCatalog(users))

        launchFriends(rule) {
            //no operations
        } verify {
            friendInformationIsDisplayedFor(friendTom)
        }
    }

    @Test
    fun showBackendError() {
        val friendsLoad: suspend () -> List<Friend> = { throw BackendException() }
        replaceUserCatalogWith(ControllableUserCatalog(friendsLoad = friendsLoad))

        launchFriends(rule) {
            //no operations
        } verify {
            backendErrorIsDisplayed()
        }
    }

    @Test
    fun showOfflineError() {
        val friendsLoad: suspend () -> List<Friend> = { throw ConnectionUnavailableException() }
        replaceUserCatalogWith(ControllableUserCatalog(friendsLoad = friendsLoad))

        launchFriends(rule) {
            //no operations
        } verify {
            offlineErrorIsDisplayed()
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
