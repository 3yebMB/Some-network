package dev.m13d.somenet.timeline

import androidx.compose.ui.test.assertIsDisplayed
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
    private val rule: ComposeTestRule
) {

    fun tapOnCreateNewPost() {
        val createNewPost = rule.activity.getString(R.string.createNewPost)
        rule.onNodeWithTag(createNewPost)
            .performClick()
    }

    infix fun verify(
        block: TimelineVerification.() -> Unit,
    ): TimelineVerification {
        return TimelineVerification(rule).apply(block)
    }
}

class TimelineVerification(
    private val rule: ComposeTestRule
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
        rule.onNodeWithTag(loading)
            .assertIsDisplayed()
    }
}
