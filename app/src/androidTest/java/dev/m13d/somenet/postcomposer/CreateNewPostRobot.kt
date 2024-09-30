package dev.m13d.somenet.postcomposer

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dev.m13d.somenet.R
import dev.m13d.somenet.timeline.launchTimelineFor
import dev.m13d.somenet.utils.ComposeTestRule

fun launchPostComposerFor(
    email: String,
    rule: ComposeTestRule,
    block: CreateNewPostRobot.() -> Unit,
): CreateNewPostRobot {
    launchTimelineFor(email, "s0mEP@ss123", rule) {
        tapOnCreateNewPost()
    }
    return CreateNewPostRobot(rule).apply(block)
}

class CreateNewPostRobot(
    private val rule: ComposeTestRule
) {
    fun typePost(postContent: String) {
        val newPostHint = rule.activity.getString(R.string.newPostHint)
        rule.onNodeWithText(newPostHint)
            .performTextInput(postContent)
    }

    fun submit() {
        val submitPost = rule.activity.getString(R.string.submitPost)
        rule.onNodeWithTag(submitPost)
            .performClick()
    }

    fun tapOnCreateNewPost() {
        val createNewPost = rule.activity.getString(R.string.createNewPost)
        rule.onNodeWithTag(createNewPost)
            .performClick()
    }

    infix fun verify(
        block: CreateNewPostVerificationRobot.() -> Unit
    ): CreateNewPostVerificationRobot {
        return CreateNewPostVerificationRobot(rule).apply(block)
    }
}

class CreateNewPostVerificationRobot(
    private val rule: ComposeTestRule
) {
    fun newCreatedPostIsShown(
        userId: String,
        dateTime: String,
        postContent: String,
    ) {
        rule.onAllNodesWithText(userId).onFirst().assertIsDisplayed()
        rule.onAllNodesWithText(userId).onLast().assertIsDisplayed()
        rule.onAllNodesWithText(dateTime).onFirst().assertIsDisplayed()
        rule.onAllNodesWithText(dateTime).onLast().assertIsDisplayed()
        rule.onNodeWithText(postContent).assertIsDisplayed()
    }

    fun backendErrorIsDisplayed() {
        val errorMessage = rule.activity.getString(R.string.creatingPostError)
        rule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    fun offlineErrorIsDisplayed() {
        val offlineMessage = rule.activity.getString(R.string.offlineError)
        rule.onNodeWithText(offlineMessage)
            .assertIsDisplayed()
    }
}
