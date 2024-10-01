package dev.m13d.somenet.friends

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dev.m13d.somenet.MainActivity
import org.junit.Rule
import org.junit.Test

class FriendsScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun displayFriends() {
        launchFriendsFor("email@mail.com", rule) {
            tapOnFriends()
        } verify {
            friendsScreenIsPresent()
        }
    }
}
