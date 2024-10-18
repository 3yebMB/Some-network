package dev.m13d.somenet.postcomposer

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.m13d.somenet.R
import dev.m13d.somenet.app.CoroutineDispatchers
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.domain.post.PostRepository
import dev.m13d.somenet.postcomposer.states.CreateNewPostScreenState
import dev.m13d.somenet.postcomposer.states.CreatePostState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreatePostViewModel(
    private val postRepository: PostRepository,
    private val dispatchers: CoroutineDispatchers,
) : ViewModel() {

    private companion object {
        private const val SCREEN_STATE_KEY = "createPostScreenState"
    }

    private var _postState = MutableLiveData<CreatePostState>()
    val postState: LiveData<CreatePostState> = _postState

    private val savedStateHandle = SavedStateHandle()
    val screenState: LiveData<CreateNewPostScreenState> = savedStateHandle.getLiveData(SCREEN_STATE_KEY)

    fun createPost(postText: String) {
        viewModelScope.launch {
            _postState.value = CreatePostState.Loading
            setLoading()
            val result = withContext(dispatchers.background) {
                postRepository.createNewPost(postText)
            }
            _postState.value = result
            updateScreenStateFor(result)
        }
    }

    fun updatePostText(postText: String) {
        val currentState = currentScreenState()
        updateScreenState(currentState.copy(postText = postText))
    }

    private fun setLoading() {
        val currentState = currentScreenState()
        updateScreenState(currentState.copy(isLoading = true))
    }

    private fun updateScreenStateFor(createPostState: CreatePostState) {
        when (createPostState) {
            is CreatePostState.Created -> setPostCreated(createPostState.post)
            is CreatePostState.BackendError -> setError(R.string.creatingPostError)
            is CreatePostState.OfflineError -> setError(R.string.offlineError)
            else -> {}
        }
    }

    private fun setError(@StringRes errorResource: Int) {
        val currentState = currentScreenState()
        updateScreenState(currentState.copy(isLoading = false, error = errorResource))
    }

    private fun setPostCreated(post: Post) {
        val currentState = currentScreenState()
        updateScreenState(currentState.copy(isLoading = false, createdPostId = post.id))
    }

    private fun currentScreenState(): CreateNewPostScreenState {
        return savedStateHandle[SCREEN_STATE_KEY] ?: CreateNewPostScreenState()
    }

    private fun updateScreenState(newScreenState: CreateNewPostScreenState) {
        savedStateHandle[SCREEN_STATE_KEY] = newScreenState
    }
}
