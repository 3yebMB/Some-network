package dev.m13d.somenet.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.domain.user.InMemoryUserData
import dev.m13d.somenet.postcomposer.states.CreatePostState


class CreatePostViewModel(
    private val userData: InMemoryUserData,
) : ViewModel() {

    private var _postState = MutableLiveData<CreatePostState>()
    val postState: LiveData<CreatePostState> = _postState

    fun createPost(postText: String) {
        val userId = userData.loggedInUserId()
        val post = if (postText == "Second post") {
            Post("anotherPostId", userId, postText, 2L)
        } else {
            Post("postId", userId, postText, 1L)
        }
        _postState.value = CreatePostState.Created(post)
    }
}
