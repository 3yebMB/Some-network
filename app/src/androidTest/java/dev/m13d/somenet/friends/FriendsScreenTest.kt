package dev.m13d.somenet.friends

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dev.m13d.somenet.MainActivity
import dev.m13d.somenet.domain.post.InMemoryPostsCatalog
import dev.m13d.somenet.domain.user.Friend
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.domain.user.UserCatalog
import org.junit.After
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class FriendsScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    @Ignore("Going to filtering showing ourself")
    fun showEmptyFriendsMessage() {
        launchFriends(rule) {
            //no operations
        } verify {
            emptyFriendsMessageIsDisplayed()
        }
    }

    @Test
    fun showAvailableFriends() {
        val tom = User("tomId", "tom@somenet.dev", "")
        val jerry = User("jerryId", "jerry@somenet.dev", "")
        val users = mutableMapOf("" to mutableListOf(tom, jerry))
        val friendTom = Friend(tom, isFollower = false)
        val friendJerry = Friend(jerry, isFollower = false)
        replaceUserCatalogWith(InMemoryUserCatalog(users))

        launchFriends(rule) {
            //no operations
        } verify {
            friendsAreDisplayed(friendTom, friendJerry)
        }
    }

    @After
    fun tearDown() {
        replaceUserCatalogWith(InMemoryUserCatalog())
    }

    private fun replaceUserCatalogWith(userCatalog: UserCatalog) {
        val replaceModule = module {
            factory<UserCatalog> { userCatalog }
        }
        loadKoinModules(replaceModule)
    }
}
