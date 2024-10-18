package dev.m13d.somenet.postcomposer

import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.app.TestDispatchers
import dev.m13d.somenet.domain.post.InMemoryPostsCatalog
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.domain.post.PostRepository
import dev.m13d.somenet.domain.user.InMemoryUserDataStore
import dev.m13d.somenet.infrastructure.ControllableClock
import dev.m13d.somenet.infrastructure.ControllableIdGenerator
import dev.m13d.somenet.postcomposer.states.CreateNewPostScreenState
import dev.m13d.somenet.postcomposer.states.CreatePostState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals


@ExperimentalCoroutinesApi
@ExtendWith(InstantTaskExecutorExtension::class)
class RenderingCreatePostStateTest {

    private val loggedInUserId = "userId"
    private val postId = "postId"
    private val timestamp = 3L
    private val postText = "Text post"
    private val post = Post(postId, loggedInUserId, postText, timestamp)

    private val idGenerator = ControllableIdGenerator(postId)
    private val clock = ControllableClock(timestamp)
    private val postCatalog = InMemoryPostsCatalog(idGenerator = idGenerator, clock = clock)
    private val userData = InMemoryUserDataStore(loggedInUserId)
    private val postRepository = PostRepository(userData, postCatalog)
    private val dispatchers = TestDispatchers()
    private val viewModel = CreatePostViewModel(postRepository, dispatchers)

    @Test
    fun uiStatesAreDeliveredInParticularOrder() {
        val deliveredStates = mutableListOf<CreateNewPostScreenState>()
        viewModel.screenState.observeForever { deliveredStates.add(it) }

        viewModel.createPost(postText)

        assertEquals(
            listOf(CreateNewPostScreenState(isLoading = true), CreateNewPostScreenState(createdPostId = post.id)),
            deliveredStates
        )
    }
}
