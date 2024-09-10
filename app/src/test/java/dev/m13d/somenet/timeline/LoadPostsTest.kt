package dev.m13d.somenet.timeline

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.infrastructure.builder.UserBuilder.Companion.aUser
import dev.m13d.somenet.timeline.states.TimelineState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class LoadPostsTest {

    @Test
    fun noPostsAvailable() {
        val viewModel = TimelineViewModel()
        viewModel.timelineFor("annaId")
        assertEquals(TimelineState.Posts(emptyList()), viewModel.timelineState.value)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun postsAvailable() {
        val tim = aUser().withId("timId").build()
        val timPosts = listOf(Post("postId", tim.userId, "post text", 1L))
        val viewModel = TimelineViewModel()
        viewModel.timelineFor(tim.userId)
        assertEquals(TimelineState.Posts(timPosts), viewModel.timelineState.value)
    }
}
