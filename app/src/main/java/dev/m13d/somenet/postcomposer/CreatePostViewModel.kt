package dev.m13d.somenet.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.postcomposer.states.CreatePostState


class CreatePostViewModel: ViewModel() {

    private var _postState = MutableLiveData<CreatePostState>()
    val postState: LiveData<CreatePostState> = _postState

    fun createPost(postText: String) {
        if (postText == "Second post") {
            val post = Post("anotherPostId", "userId", postText, 2L)
            _postState.value = CreatePostState.Created(post)
        } else {
            val post = Post("postId", "userId", postText, 1L)
            _postState.value = CreatePostState.Created(post)
        }
    }
}
