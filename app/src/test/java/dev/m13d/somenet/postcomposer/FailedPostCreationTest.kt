package dev.m13d.somenet.postcomposer

import androidx.lifecycle.SavedStateHandle
import dev.m13d.somenet.InstantTaskExecutorExtension
import dev.m13d.somenet.R
import dev.m13d.somenet.app.TestDispatchers
import dev.m13d.somenet.domain.post.OfflinePostCatalog
import dev.m13d.somenet.domain.post.PostRepository
import dev.m13d.somenet.domain.post.UnavailablePostCatalog
import dev.m13d.somenet.domain.user.InMemoryUserDataStore
import dev.m13d.somenet.postcomposer.states.CreateNewPostScreenState
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
                InMemoryUserDataStore("userId"),
                UnavailablePostCatalog()
            ),
            SavedStateHandle(),
            TestDispatchers(),
        )

        viewModel.createPost(":backend:")

        assertEquals(CreateNewPostScreenState(error = R.string.creatingPostError), viewModel.screenState.value)
    }

    @Test
    fun offlineError() {
        val viewModel = CreatePostViewModel(
            PostRepository(
                InMemoryUserDataStore("userId"),
                OfflinePostCatalog()
            ),
            SavedStateHandle(),
            TestDispatchers(),
        )

        viewModel.createPost(":offline:")

        assertEquals(CreateNewPostScreenState(error = R.string.offlineError), viewModel.screenState.value)
    }
}
