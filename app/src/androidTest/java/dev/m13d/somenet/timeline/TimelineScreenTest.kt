package dev.m13d.somenet.timeline

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dev.m13d.somenet.MainActivity
import org.junit.Rule
import org.junit.Test

class TimelineScreenTest {

    @get:Rule
    val timelineTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun showEmptyTimelineMessage() {
        val email = "elizabeth@somenet.dev"
        val password = "p@S\$w0rd="
        launchTimelineFor(email, password, timelineTestRule) {
            // No operations
        } verify {
            emptyTimelineMessageIsShown()
        }
    }
}
