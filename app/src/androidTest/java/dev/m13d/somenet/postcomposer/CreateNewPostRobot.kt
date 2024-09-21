package dev.m13d.somenet.postcomposer

import androidx.compose.ui.test.assertIsDisplayed
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
        rule.onNodeWithText(userId).assertIsDisplayed()
        rule.onNodeWithText(dateTime).assertIsDisplayed()
        rule.onNodeWithText(postContent).assertIsDisplayed()
    }
}
