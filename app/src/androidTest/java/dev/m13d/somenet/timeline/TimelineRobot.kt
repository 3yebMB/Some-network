package dev.m13d.somenet.timeline

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.m13d.somenet.R
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.signup.launchSignUpScreen
import dev.m13d.somenet.utils.ComposeTestRule

fun launchTimelineFor(
    email: String,
    password: String,
    rule: ComposeTestRule,
    block: TimelineRobot.() -> Unit,
): TimelineRobot {
    launchSignUpScreen(rule) {
        typeEmail(email)
        typePassword(password)
        submit()
    }
    return TimelineRobot(rule).apply(block)
}

class TimelineRobot(
    private val rule: ComposeTestRule,
) {

    fun tapOnCreateNewPost() {
        val createNewPost = rule.activity.getString(R.string.createNewPost)
        rule.onNodeWithTag(createNewPost)
            .performClick()
    }

    fun tapOnFriendsTab() {
        val friends = rule.activity.getString(R.string.friends)
        rule.onNodeWithText(friends)
            .performClick()
    }

    infix fun verify(
        block: TimelineVerification.() -> Unit,
    ): TimelineVerification {
        return TimelineVerification(rule).apply(block)
    }
}

class TimelineVerification(
    private val rule: ComposeTestRule,
) {
    fun emptyTimelineMessageIsShown() {
        val emptyTimelineMessage = rule.activity.getString(R.string.emptyTimelineMessage)
        rule.onNodeWithText(emptyTimelineMessage)
            .assertIsDisplayed()
    }

    fun postsAreDisplayed(vararg posts: Post) {
        posts.forEach { post ->
            rule.onNodeWithText(post.text)
                .assertIsDisplayed()
        }
    }

    fun newPostComposerIsDisplayed() {
        val createNewPost = rule.activity.getString(R.string.createNewPost)
        rule.onNodeWithText(createNewPost)
            .assertIsDisplayed()
    }

    fun loadingIndicatorIsDisplayed() {
        val loading = rule.activity.getString(R.string.loading)
        rule.onNodeWithContentDescription(loading)
            .assertIsDisplayed()
    }

    fun backendErrorIsDisplayed() {
        val errorMessage = rule.activity.getString(R.string.fetchingTimelineError)
        rule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    fun offlineErrorIsDisplayed() {
        val errorMessage = rule.activity.getString(R.string.offlineError)
        rule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    fun friendsScreenIsDisplayed() {
        val friends = rule.activity.getString(R.string.friends)
        rule.onAllNodesWithText(friends)
            .onFirst()
            .assertIsDisplayed()
    }
}
