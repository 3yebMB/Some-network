package dev.m13d.somenet.timeline

import androidx.lifecycle.SavedStateHandle
import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.app.TestDispatchers
import dev.m13d.somenet.domain.post.InMemoryPostsCatalog
import dev.m13d.somenet.domain.timeline.TimelineRepository
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.timeline.states.TimelineScreenState
import dev.m13d.somenet.timeline.states.TimelineState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class RenderingTimelineStatesTest {

    private val timelineRepository = TimelineRepository(
        InMemoryUserCatalog(),
        InMemoryPostsCatalog(),
    )
    private val viewModel = TimelineViewModel(
        timelineRepository,
        SavedStateHandle(),
        TestDispatchers(),
    )

    @Test
    fun timelineStatesExposedToObserver() {
        val renderedStates = mutableListOf<TimelineScreenState>()

        viewModel.timelineScreenState.observeForever { renderedStates.add(it) }
        viewModel.timelineFor(":irrelevant:")

        assertEquals(
            listOf(
                TimelineScreenState(isLoading = true),
                TimelineScreenState(posts = emptyList())
            ),
            renderedStates
        )
    }
}
