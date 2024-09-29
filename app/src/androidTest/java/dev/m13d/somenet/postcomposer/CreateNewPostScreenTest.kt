package dev.m13d.somenet.postcomposer

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dev.m13d.somenet.MainActivity
import dev.m13d.somenet.domain.post.InMemoryPostsCatalog
import dev.m13d.somenet.domain.post.PostsCatalog
import dev.m13d.somenet.domain.user.InMemoryUserData
import dev.m13d.somenet.infrastructure.ControllableClock
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.time.LocalDateTime
import java.time.ZoneOffset

class CreateNewPostScreenTest {

    @get:Rule
    val createNewPostRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun createNewPost() {
        val timestampWithTimezoneOffset = LocalDateTime
            .of(2024, 9, 21, 15, 30)
            .toInstant(ZoneOffset.ofTotalSeconds(0))
            .toEpochMilli()

        replaceUserDataWith(InMemoryUserData("mihalyId"))
        replacePostCatalogWith(InMemoryPostsCatalog(clock = ControllableClock(timestampWithTimezoneOffset)))

        launchPostComposerFor("mihaly@somenet.dev", createNewPostRule) {
            typePost("My New Post")
            submit()
        } verify {
            newCreatedPostIsShown("mihalyId", "21-09-2024 17:30", "My New Post")
        }
    }

    @After
    fun tearDown() {
        replacePostCatalogWith(InMemoryPostsCatalog())
        replaceUserDataWith(InMemoryUserData(""))
    }

    private fun replacePostCatalogWith(postsCatalog: PostsCatalog) {
        val module = module {
            single { postsCatalog }
        }
        loadKoinModules(module)
    }

    private fun replaceUserDataWith(userData: InMemoryUserData) {
        val module = module {
            single { userData }
        }
        loadKoinModules(module)
    }
}

