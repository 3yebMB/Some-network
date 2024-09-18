package dev.m13d.somenet.postcomposer

import dev.m13d.somenet.InstantTaskExecutorExtension
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
class FailedPostCreationTest {

    @Test
    fun backendError() {
        val viewModel = CreatePostViewModel(
            InMemoryUserData("userId"),
            ControllableClock(1L),
            ControllableIdGenerator("post1Id")
        )

        viewModel.createPost(":backend:")

        assertEquals(CreatePostState.BackendError, viewModel.postState.value)
    }

    @Test
    fun offlineError() {
        val viewModel = CreatePostViewModel(
            InMemoryUserData("userId"),
            ControllableClock(1L),
            ControllableIdGenerator("post2Id")
        )

        viewModel.createPost(":offline:")

        assertEquals(CreatePostState.OfflineError, viewModel.postState.value)
    }
}
