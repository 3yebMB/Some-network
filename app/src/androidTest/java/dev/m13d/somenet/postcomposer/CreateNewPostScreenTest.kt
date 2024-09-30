package dev.m13d.somenet.postcomposer

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dev.m13d.somenet.MainActivity
import dev.m13d.somenet.domain.post.DelayingPostCatalog
import dev.m13d.somenet.domain.post.InMemoryPostsCatalog
import dev.m13d.somenet.domain.post.OfflinePostCatalog
import dev.m13d.somenet.domain.post.PostsCatalog
import dev.m13d.somenet.domain.post.UnavailablePostCatalog
import dev.m13d.somenet.domain.user.InMemoryUserDataStore
import dev.m13d.somenet.domain.user.UserDataStore
import dev.m13d.somenet.infrastructure.ControllableClock
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import java.util.Calendar

class CreateNewPostScreenTest {

    @get:Rule
    val createNewPostRule = createAndroidComposeRule<MainActivity>()

    private val timestamp = Calendar.getInstance()
        .also { it.set(2024, 8, 21, 17, 30) }
        .timeInMillis

    @Test
    fun createNewPost() {
        replacePostCatalogWith(InMemoryPostsCatalog(clock = ControllableClock(timestamp)))

        launchPostComposerFor("mihaly@somenet.dev", createNewPostRule) {
            typePost("My New Post")
            submit()
        } verify {
            newCreatedPostIsShown("mihalyId", "21-09-2024 17:30", "My New Post")
        }
    }

    @Test
    fun createMultiplePost() {
        replacePostCatalogWith(InMemoryPostsCatalog(clock = ControllableClock(timestamp)))

        launchPostComposerFor("mihaly@somenet.dev", createNewPostRule) {
            typePost("My First Post")
            submit()
            tapOnCreateNewPost()
            typePost("My Second Post")
            submit()
        } verify {
            newCreatedPostIsShown("mihalyId", "21-09-2024 17:30", "My First Post")
            newCreatedPostIsShown("mihalyId", "21-09-2024 17:30", "My Second Post")
        }
    }

    @Test
    fun showLoadingBlock() {
        replacePostCatalogWith(DelayingPostCatalog())

        launchPostComposerFor("robert@somenet.dev", createNewPostRule) {
            typePost("Waiting")
            submit()
        } verify {
            loadingBlockIsShown()
        }
    }

    @Test
    fun showBackendError() {
        replacePostCatalogWith(UnavailablePostCatalog())

        launchPostComposerFor("dan@somenet.dev", createNewPostRule) {
            typePost("Some Post")
            submit()
        } verify {
            backendErrorIsDisplayed()
        }
    }

    @Test
    fun showOfflineError() {
        replacePostCatalogWith(OfflinePostCatalog())

        launchPostComposerFor("mia@somenet.dev", createNewPostRule) {
            typePost("My New Post")
            submit()
        } verify {
            offlineErrorIsDisplayed()
        }
    }

    @After
    fun tearDown() {
        replacePostCatalogWith(InMemoryPostsCatalog())
        replaceUserDataWith(InMemoryUserDataStore())
    }

    private fun replacePostCatalogWith(postsCatalog: PostsCatalog) {
        val module = module {
            single { postsCatalog }
        }
        loadKoinModules(module)
    }

    private fun replaceUserDataWith(userData: UserDataStore) {
        val module = module {
            single { userData }
        }
        loadKoinModules(module)
    }
}

