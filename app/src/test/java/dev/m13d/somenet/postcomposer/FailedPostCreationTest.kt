package dev.m13d.somenet.postcomposer

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.domain.post.PostRepository
import dev.m13d.somenet.domain.post.PostsCatalog
import dev.m13d.somenet.domain.user.InMemoryUserData
import dev.m13d.somenet.postcomposer.states.CreatePostState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class FailedPostCreationTest {

    @Test
    fun backendError() {
        val viewModel = CreatePostViewModel(
            PostRepository(
                InMemoryUserData("userId"),
                UnavailablePostCatalog()
            )
        )

        viewModel.createPost(":backend:")

        assertEquals(CreatePostState.BackendError, viewModel.postState.value)
    }

    @Test
    fun offlineError() {
        val viewModel = CreatePostViewModel(
            PostRepository(
                InMemoryUserData("userId"),
                OfflinePostCatalog()
            )
        )

        viewModel.createPost(":offline:")

        assertEquals(CreatePostState.OfflineError, viewModel.postState.value)
    }

    private class UnavailablePostCatalog : PostsCatalog {
        override suspend fun postsFor(userIds: List<String>): List<Post> {
            TODO("Not yet implemented")
        }

        override fun addPost(userId: String, postText: String): Post {
            throw BackendException()
        }

    }


    private class OfflinePostCatalog : PostsCatalog {
        override suspend fun postsFor(userIds: List<String>): List<Post> {
            TODO("Not yet implemented")
        }

        override fun addPost(userId: String, postText: String): Post {
            throw ConnectionUnavailableException()
        }

    }
}
