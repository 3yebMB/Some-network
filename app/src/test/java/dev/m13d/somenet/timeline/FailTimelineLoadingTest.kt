package dev.m13d.somenet.timeline

import androidx.lifecycle.SavedStateHandle
import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.app.TestDispatchers
import dev.m13d.somenet.domain.post.OfflinePostCatalog
import dev.m13d.somenet.domain.post.UnavailablePostCatalog
import dev.m13d.somenet.domain.timeline.TimelineRepository
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.timeline.states.TimelineScreenState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import dev.m13d.somenet.R

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class FailTimelineLoadingTest {

    @Test
    fun backendError() {
        val userCatalog = InMemoryUserCatalog()
        val postCatalog = UnavailablePostCatalog()
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog),
            SavedStateHandle(),
            TestDispatchers(),
        )

        viewModel.timelineFor(":irrelevant:")

        assertEquals(
            TimelineScreenState(error = R.string.fetchingTimelineError),
            viewModel.screenState.value
        )
    }

    @Test
    fun offlineError() {
        val userCatalog = InMemoryUserCatalog()
        val postCatalog = OfflinePostCatalog()
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog),
            SavedStateHandle(),
            TestDispatchers(),
        )

        viewModel.timelineFor(":irrelevant:")

        assertEquals(
            TimelineScreenState(error = R.string.offlineError),
            viewModel.screenState.value
        )
    }
}
