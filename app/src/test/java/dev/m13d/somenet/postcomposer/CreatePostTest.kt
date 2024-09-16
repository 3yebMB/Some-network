package dev.m13d.somenet.postcomposer

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.postcomposer.states.CreatePostState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class CreatePostTest {

    @Test
    fun postIsCreated() {

        val viewModel = CreatePostViewModel()

        val postText = "First post"
        val post = Post("postId", "userId", postText, 1L)
        viewModel.createPost(postText)


        assertEquals(CreatePostState.Created(post), viewModel.postState.value)
    }
}
