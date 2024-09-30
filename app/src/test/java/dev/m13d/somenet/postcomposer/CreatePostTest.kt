package dev.m13d.somenet.postcomposer

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.app.TestDispatchers
import dev.m13d.somenet.domain.post.InMemoryPostsCatalog
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.domain.post.PostRepository
import dev.m13d.somenet.domain.user.InMemoryUserDataStore
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

    @Test
    fun postIsCreated() {
        val postText = "First post"
        val post = Post("postId", "userId", postText, 1L)
        val userData = InMemoryUserDataStore("userId")
        val clock = ControllableClock(1L)
        val idGenerator = ControllableIdGenerator("postId")
        val viewModel = CreatePostViewModel(
            PostRepository(
                userData,
                InMemoryPostsCatalog(
                    idGenerator = idGenerator,
                    clock = clock,
                )
            ),
            TestDispatchers(),
        )

        viewModel.createPost(postText)

        assertEquals(CreatePostState.Created(post), viewModel.postState.value)
    }

    @Test
    fun anotherPostCreated() {
        val postText = "Second post"
        val anotherPost = Post("post2Id", "userId", postText, 2L)
        val userData = InMemoryUserDataStore("userId")
        val clock = ControllableClock(2L)
        val idGenerator = ControllableIdGenerator("post2Id")
        val viewModel = CreatePostViewModel(
            PostRepository(
                userData,
                InMemoryPostsCatalog(
                    idGenerator = idGenerator,
                    clock = clock,
                )
            ),
            TestDispatchers(),
        )

        viewModel.createPost(postText)

        assertEquals(CreatePostState.Created(anotherPost), viewModel.postState.value)
    }
}
