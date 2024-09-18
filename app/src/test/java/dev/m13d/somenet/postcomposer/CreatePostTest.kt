package dev.m13d.somenet.postcomposer

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.domain.user.InMemoryUserData
import dev.m13d.somenet.infrastructure.ControllableClock
import dev.m13d.somenet.infrastructure.ControllableIdGenerator
import dev.m13d.somenet.postcomposer.states.CreatePostState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class CreatePostTest {

    private val userId = "userId"
    private val userData = InMemoryUserData(userId)

    @Test
    fun postIsCreated() {
        val postText = "First post"
        val post = Post("postId", "userId", postText, 1L)
        val viewModel = CreatePostViewModel(
            userData,
            ControllableClock(1L),
            ControllableIdGenerator("postId")
        )

        viewModel.createPost(postText)

        assertEquals(CreatePostState.Created(post), viewModel.postState.value)
    }

    @Test
    fun anotherPostCreated() {
        val postText = "Second post"
        val anotherPost = Post("post2Id", "userId", postText, 2L)
        val viewModel = CreatePostViewModel(
            userData,
            ControllableClock(2L),
            ControllableIdGenerator("post2Id")
        )

        viewModel.createPost(postText)

        assertEquals(CreatePostState.Created(anotherPost), viewModel.postState.value)
    }
}
