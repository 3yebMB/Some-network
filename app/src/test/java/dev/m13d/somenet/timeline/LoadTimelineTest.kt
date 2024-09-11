package dev.m13d.somenet.timeline

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.domain.post.InMemoryPostsCatalog
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.domain.timeline.TimelineRepository
import dev.m13d.somenet.domain.user.Following
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.infrastructure.builder.UserBuilder.Companion.aUser
import dev.m13d.somenet.timeline.states.TimelineState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi

@ExperimentalUuidApi
@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class LoadTimelineTest {

    private val lucy = aUser().withId("lucyId").build()
    private val sarah = aUser().withId("sarahId").build()
    private val anabel = aUser().withId("anabelId").build()
    private val tim = aUser().withId("timId").build()

    private val timPosts = listOf(
        Post("postId", tim.id, "post text", 1L),
    )
    private val lucyPosts = listOf(
        Post("post2", lucy.id, "post 2", 2L),
        Post("post1", lucy.id, "post 1", 1L),
    )
    private val sarahPosts = listOf(
        Post("post4", sarah.id, "post 4", 4L),
        Post("post3", sarah.id, "post 3", 3L),
    )

    private val availablePosts = timPosts + lucyPosts + sarahPosts

    @Test
    fun noPostsAvailable() {
        val userCatalog = InMemoryUserCatalog()
        val postCatalog = InMemoryPostsCatalog(availablePosts)
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog)
        )
        viewModel.timelineFor("annaId")
        assertEquals(TimelineState.Posts(emptyList()), viewModel.timelineState.value)
    }

    @Test
    fun postsAvailable() {
        val userCatalog = InMemoryUserCatalog()
        val postCatalog = InMemoryPostsCatalog(availablePosts)
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog)
        )
        viewModel.timelineFor(tim.id)
        assertEquals(TimelineState.Posts(timPosts), viewModel.timelineState.value)
    }

    @Test
    fun postsFromFriends() {
        val userCatalog = InMemoryUserCatalog(
            followings = listOf(Following(anabel.id, lucy.id))
        )
        val postCatalog = InMemoryPostsCatalog(availablePosts)
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog)
        )
        viewModel.timelineFor(anabel.id)
        assertEquals(TimelineState.Posts(lucyPosts), viewModel.timelineState.value)
    }

    @Test
    fun allPostsAvailable() {
        val userCatalog = InMemoryUserCatalog(
            followings = listOf(Following(sarah.id, lucy.id))
        )
        val postCatalog = InMemoryPostsCatalog(availablePosts)
        val viewModel = TimelineViewModel(
            TimelineRepository(userCatalog, postCatalog)
        )
        viewModel.timelineFor(sarah.id)
        assertEquals(TimelineState.Posts(lucyPosts + sarahPosts), viewModel.timelineState.value)
    }
}