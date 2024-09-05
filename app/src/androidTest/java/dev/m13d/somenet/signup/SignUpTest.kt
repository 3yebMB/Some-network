package dev.m13d.somenet.signup

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dev.m13d.somenet.MainActivity
import org.junit.Rule
import org.junit.Test

class SignUpTest {

    @get:Rule
    val signUpTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun performSignUp() {
        launchSignUpScreen(signUpTestRule) {
            typeEmail("somemail@network.app")
            typePassword("p@\$Sw0rd")
            submit()
        } verify {
            timelineScreenIsPresent()
        }
    }
}
