package dev.m13d.somenet.friends

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dev.m13d.somenet.MainActivity
import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
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
        val loadFriend: suspend () -> List<Friend> = {
            delay(1500L)
            listOf(friendTom, friendJerry)
        }
        replaceUserCatalogWith(DelayingUserCatalog(loadFriend = loadFriend))

        launchFriends(rule) {
            //no operations
        } verify {
            loadingIndicatorIsShown()
        }
    }

    private class DelayingUserCatalog(
        private val followedBy: suspend () -> List<String> = { emptyList() },
        private val loadFriend: suspend () -> List<Friend> = { emptyList() },
    ) : UserCatalog {

        override suspend fun createUser(email: String, password: String, about: String): User {
            return User(":irrelevant:", email, about)
        }

        override suspend fun followedBy(userId: String): List<String> {
            return followedBy()
        }

        override suspend fun loadFriendsFor(userId: String): List<Friend> {
            return loadFriend()
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
        val loadFriend: suspend () -> List<Friend> = { throw BackendException() }
        replaceUserCatalogWith(DelayingUserCatalog(loadFriend = loadFriend))

        launchFriends(rule) {
            //no operations
        } verify {
            backendErrorIsDisplayed()
        }
    }

    @Test
    fun showOfflineError() {
        val loadFriend: suspend () -> List<Friend> = { throw ConnectionUnavailableException() }
        replaceUserCatalogWith(DelayingUserCatalog(loadFriend = loadFriend))

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
