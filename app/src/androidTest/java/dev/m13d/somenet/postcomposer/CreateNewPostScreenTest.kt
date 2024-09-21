package dev.m13d.somenet.postcomposer

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dev.m13d.somenet.MainActivity
import dev.m13d.somenet.domain.user.InMemoryUserData
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class CreateNewPostScreenTest {

    @get:Rule
    val createNewPostRule = createAndroidComposeRule<MainActivity>()

    @Ignore("Under construction")
    @Test
    fun createNewPost() {
        replaceUserDataWith(InMemoryUserData("mihalyId"))
        launchPostComposerFor("mihaly@somenet.dev", createNewPostRule) {
            typePost("My New Post")
            submit()
        } verify {
            newCreatedPostIsShown("mihalyId", "21-09-2024 17:30", "My New Post")
        }
    }

    private fun replaceUserDataWith(userData: InMemoryUserData) {
        val module = module {
            single { userData }
        }
        loadKoinModules(module)
    }
}

