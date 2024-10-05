package dev.m13d.somenet.timeline

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dev.m13d.somenet.MainActivity
import dev.m13d.somenet.domain.post.DelayingPostCatalog
import dev.m13d.somenet.domain.post.InMemoryPostsCatalog
import dev.m13d.somenet.domain.post.OfflinePostCatalog
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.domain.post.PostsCatalog
import dev.m13d.somenet.domain.post.UnavailablePostCatalog
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class TimelineScreenTest {

    @get:Rule
    val timelineTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun showEmptyTimelineMessage() {
        val email = "elizabeth@somenet.dev"
        val password = "p@Ssw0rd="
        launchTimelineFor(email, password, timelineTestRule) {
            // No operations
        } verify {
            emptyTimelineMessageIsShown()
        }
    }

    @Test
    fun showAvailablePosts() {
        val email = "samantha@somenet.dev"
        val password = "B@rb24A89"
        val post1 = Post("post1", "samanthaId", "This is Samantha's first post", 1L)
        val post2 = Post("post2", "samanthaId", "This is Samantha's second post", 2L)
        replacePostCatalogWith(InMemoryPostsCatalog(mutableListOf(post1, post2)))

        launchTimelineFor(email, password, timelineTestRule) {
            //no operations
        } verify {
            postsAreDisplayed(post1, post2)
        }
    }

    @Test
    fun openPostComposer() {
        launchTimelineFor("test@somenet.dev", "S0meP@sS1134", timelineTestRule) {
            tapOnCreateNewPost()
        } verify {
            newPostComposerIsDisplayed()
        }
    }

    @Test
    fun opensFriends() {
        launchTimelineFor("sam@mail.com", "P@sSw0rd", timelineTestRule) {
            tapOnFriendsTab()
        } verify {
            friendsScreenIsDisplayed()
        }
    }

    @Test
    fun showLoadingIndicator() {
        replacePostCatalogWith(DelayingPostCatalog())
        launchTimelineFor("test-indicator@somenet.dev", "S0meP@sS1134", timelineTestRule) {
            //no operations
        } verify {
            loadingIndicatorIsDisplayed()
        }
    }

    @Test
    fun showBackendError() {
        replacePostCatalogWith(UnavailablePostCatalog())
        launchTimelineFor("backend-error@somenet.dev", "S0meP@sS1134", timelineTestRule) {
            //no operations
        } verify {
            backendErrorIsDisplayed()
        }
    }

    @Test
    fun showOfflineError() {
        replacePostCatalogWith(OfflinePostCatalog())
        launchTimelineFor("offline-error@somenet.dev", "S0meP@sS1134", timelineTestRule) {
            //no operations
        } verify {
            offlineErrorIsDisplayed()
        }
    }

    @After
    fun tearDown() {
        replacePostCatalogWith(InMemoryPostsCatalog())
    }

    private fun replacePostCatalogWith(postsCatalog: PostsCatalog) {
        val replaceModule = module {
            factory { postsCatalog }
        }
        loadKoinModules(replaceModule)
    }
}
