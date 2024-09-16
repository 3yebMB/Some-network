package dev.m13d.somenet.timeline

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.app.TestDispatchers
import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.domain.post.PostsCatalog
import dev.m13d.somenet.domain.timeline.TimelineRepository
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.timeline.states.TimelineState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class FailTimelineLoadingTest {

    @Test
    fun backendError() {
        val userCatalog = InMemoryUserCatalog()
        val postCatalog = UnavailablePostCatalog()
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog),
            TestDispatchers(),
        )
        viewModel.timelineFor(":irrelevant:")
        assertEquals(TimelineState.BackendError, viewModel.timelineState.value)
    }

    @Test
    fun offlineError() {
        val userCatalog = InMemoryUserCatalog()
        val postCatalog = OfflinePostCatalog()
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog),
            TestDispatchers(),
        )
        viewModel.timelineFor(":irrelevant:")
        assertEquals(TimelineState.OfflineError, viewModel.timelineState.value)
    }

    private class UnavailablePostCatalog : PostsCatalog {
        override suspend fun postsFor(userIds: List<String>): List<Post> {
            throw BackendException()
        }
    }

    private class OfflinePostCatalog : PostsCatalog {
        override suspend fun postsFor(userIds: List<String>): List<Post> {
            throw ConnectionUnavailableException()
        }
    }
}
