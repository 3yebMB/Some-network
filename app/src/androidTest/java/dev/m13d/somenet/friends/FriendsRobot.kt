package dev.m13d.somenet.friends

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.m13d.somenet.R
import dev.m13d.somenet.timeline.launchTimelineFor
import dev.m13d.somenet.utils.ComposeTestRule

fun launchFriends(
    rule: ComposeTestRule,
    block: FriendsRobot.() -> Unit,
): FriendsRobot {
    launchTimelineFor("email@mail.com", "Pas$123.", rule) {}
    return FriendsRobot(rule).apply{
        tapOnFriends()
        block()
    }
}

class FriendsRobot(
    private val rule: ComposeTestRule,
) {
    fun tapOnFriends() {
        val friends = rule.activity.getString(R.string.friends)
        rule.onNodeWithText(friends)
            .performClick()
    }

    infix fun verify(
        block: FriendsVerification.() -> Unit,
    ): FriendsVerification {
        return FriendsVerification(rule).apply(block)
    }
}

class FriendsVerification(
    private val rule: ComposeTestRule,
) {

    fun emptyFriendsMessageIsDisplayed() {
        val emptyFriendsMessage = rule.activity.getString(R.string.emptyFriendsMessage)
        rule.onNodeWithText(emptyFriendsMessage)
            .assertIsDisplayed()
    }
}
