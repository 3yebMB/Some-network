package dev.m13d.somenet.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.domain.user.InMemoryUserData
import dev.m13d.somenet.infrastructure.ControllableClock
import dev.m13d.somenet.postcomposer.states.CreatePostState


class CreatePostViewModel(
    private val userData: InMemoryUserData,
    private val clock: ControllableClock,
) : ViewModel() {

        private var _postState = MutableLiveData<CreatePostState>()
    val postState: LiveData<CreatePostState> = _postState

    fun createPost(postText: String) {
        val userId = userData.loggedInUserId()
        val post = if (postText == "Second post") {
            Post("postId", userId, postText, clock.now())
        } else {
            Post("postId", userId, postText, clock.now())
        }
        _postState.value = CreatePostState.Created(post)
    }
}
