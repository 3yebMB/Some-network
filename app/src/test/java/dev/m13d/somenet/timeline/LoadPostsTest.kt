package dev.m13d.somenet.timeline

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.domain.post.InMemoryPostsCatalog
import dev.m13d.somenet.domain.post.Post
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
class LoadPostsTest {

    @Test
    fun noPostsAvailable() {
        val viewModel = TimelineViewModel(
            InMemoryUserCatalog(), InMemoryPostsCatalog(
                listOf(
                    Post("post2", "lucyId", "post 2", 2L),
                    Post("post1", "lucyId", "post 1", 1L),
                    Post("postId", "timId", "post text", 1L),
                    Post("post4", "sarahId", "post 4", 4L),
                    Post("post3", "sarahId", "post 3", 3L),
                )
            )
        )
        viewModel.timelineFor("annaId")
        assertEquals(TimelineState.Posts(emptyList()), viewModel.timelineState.value)
    }

    @Test
    fun postsAvailable() {
        val tim = aUser().withId("timId").build()
        val timPosts = listOf(Post("postId", tim.id, "post text", 1L))
        val viewModel = TimelineViewModel(
            InMemoryUserCatalog(), InMemoryPostsCatalog(
                listOf(
                    Post("post2", "lucyId", "post 2", 2L),
                    Post("post1", "lucyId", "post 1", 1L),
                    Post("postId", "timId", "post text", 1L),
                    Post("post4", "sarahId", "post 4", 4L),
                    Post("post3", "sarahId", "post 3", 3L),
                )
            )
        )
        viewModel.timelineFor(tim.id)
        assertEquals(TimelineState.Posts(timPosts), viewModel.timelineState.value)
    }

    @Test
    fun postsFromFriends() {
        val anabel = aUser().withId("anabelId").build()
        val lucy = aUser().withId("lucyId").build()
        val lucyPosts = listOf(
            Post("post2", lucy.id, "post 2", 2L),
            Post("post1", lucy.id, "post 1", 1L)
        )
        val viewModel = TimelineViewModel(
            InMemoryUserCatalog(
                followings = listOf(
                    Following(anabel.id, lucy.id)
                )
            ),
            InMemoryPostsCatalog(
                listOf(
                    Post("post2", "lucyId", "post 2", 2L),
                    Post("post1", "lucyId", "post 1", 1L),
                    Post("postId", "timId", "post text", 1L),
                    Post("post4", "sarahId", "post 4", 4L),
                    Post("post3", "sarahId", "post 3", 3L),
                )
            )
        )
        viewModel.timelineFor(anabel.id)
        assertEquals(TimelineState.Posts(lucyPosts), viewModel.timelineState.value)
    }

    @Test
    fun allPostsAvailable() {
        val lucy = aUser().withId("lucyId").build()
        val lucyPosts = listOf(
            Post("post2", lucy.id, "post 2", 2L),
            Post("post1", lucy.id, "post 1", 1L),
        )
        val sarah = aUser().withId("sarahId").build()
        val sarahPosts = listOf(
            Post("post4", sarah.id, "post 4", 4L),
            Post("post3", sarah.id, "post 3", 3L),
        )

        val viewModel = TimelineViewModel(
            InMemoryUserCatalog(
                followings = listOf(
                    Following(sarah.id, lucy.id)
                )
            ),
            InMemoryPostsCatalog(
                listOf(
                    Post("post2", "lucyId", "post 2", 2L),
                    Post("post1", "lucyId", "post 1", 1L),
                    Post("postId", "timId", "post text", 1L),
                    Post("post4", "sarahId", "post 4", 4L),
                    Post("post3", "sarahId", "post 3", 3L),
                )
            )
        )
        viewModel.timelineFor(sarah.id)
        assertEquals(TimelineState.Posts(lucyPosts + sarahPosts), viewModel.timelineState.value)
    }
}
