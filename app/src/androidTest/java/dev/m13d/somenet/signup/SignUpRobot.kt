package dev.m13d.somenet.signup

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dev.m13d.somenet.R
import dev.m13d.somenet.utils.ComposeTestRule

fun launchSignUpScreen(
    rule: ComposeTestRule,
    block: SignUpRobot.() -> Unit,
): SignUpRobot {
    return SignUpRobot(rule).apply(block)
}

class SignUpRobot(
    private val rule: ComposeTestRule,
) {

    fun typeEmail(email: String) {
        val emailHint = rule.activity.getString(R.string.email)
        rule.onNodeWithTag(emailHint).performTextInput(email)
    }

    fun typePassword(password: String) {
        val passwordHint = rule.activity.getString(R.string.password)
        rule.onNodeWithTag(passwordHint).performTextInput(password)
    }

    fun submit() {
        val signUp = rule.activity.getString(R.string.signUp)
        rule.onNodeWithText(signUp).performClick()
    }

    infix fun verify(
        block: SignUpVerification.() -> Unit,
    ): SignUpVerification {
        return SignUpVerification(rule).apply(block)
    }
}

class SignUpVerification(
    private val rule: ComposeTestRule,
) {

    fun timelineScreenIsPresent() {
        val timeline = rule.activity.getString(R.string.timeline)
        rule.onNodeWithText(timeline)
            .assertIsDisplayed()
    }

    fun duplicateAccountErrorIsShown() {
        val duplicateAccountError = rule.activity.getString(R.string.duplicateAccountError)
        rule.onNodeWithText(duplicateAccountError)
            .assertIsDisplayed()
    }

    fun backendErrorIsShown() {
        val backendError = rule.activity.getString(R.string.createAccountError)
        rule.onNodeWithText(backendError)
            .assertIsDisplayed()
    }

    fun offlineErrorIsShown() {
        val offlineError = rule.activity.getString(R.string.offlineError)
        rule.onNodeWithText(offlineError)
            .assertIsDisplayed()
    }

    fun badEmailErrorIsShown() {
        val badEmail = rule.activity.getString(R.string.badEmail)
        rule.onNodeWithText(badEmail)
            .assertIsDisplayed()
    }

    fun badPasswordErrorIsShown() {
        val badPassword = rule.activity.getString(R.string.badPassword)
        rule.onNodeWithText(badPassword)
            .assertIsDisplayed()
    }

    fun badEmailErrorIsNotShown() {
        val badEmail = rule.activity.getString(R.string.badEmail)
        rule.onNodeWithText(badEmail)
            .assertDoesNotExist()
    }

    fun badPasswordErrorIsNotShown() {
        val badPassword = rule.activity.getString(R.string.badPassword)
        rule.onNodeWithText(badPassword)
            .assertDoesNotExist()
    }

    fun loadingBlockIsShown() {
        val loading = rule.activity.getString(R.string.loading)
        rule.onNodeWithTag(loading)
            .assertIsDisplayed()
    }
}
