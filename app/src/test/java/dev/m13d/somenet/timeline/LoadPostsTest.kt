package dev.m13d.somenet.timeline

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.timeline.states.TimelineState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class LoadPostsTest {

    @Test
    fun noPostsAvailable() {
        val viewModel = TimelineViewModel()
        viewModel.timelineFor("annaId")
        assertEquals(TimelineState.Posts(emptyList()), viewModel.timelineState.value)
    }
}
