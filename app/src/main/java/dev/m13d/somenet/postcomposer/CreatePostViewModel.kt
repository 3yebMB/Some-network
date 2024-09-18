package dev.m13d.somenet.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.somenet.domain.post.PostRepository
import dev.m13d.somenet.postcomposer.states.CreatePostState

class CreatePostViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {

    private var _postState = MutableLiveData<CreatePostState>()
    val postState: LiveData<CreatePostState> = _postState

    fun createPost(postText: String) {
        val result = postRepository.createNewPost(postText)
        _postState.value = result
    }
}
