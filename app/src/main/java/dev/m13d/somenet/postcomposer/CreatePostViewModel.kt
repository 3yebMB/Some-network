package dev.m13d.somenet.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.domain.user.InMemoryUserData
import dev.m13d.somenet.infrastructure.Clock
import dev.m13d.somenet.infrastructure.IdGenerator
import dev.m13d.somenet.postcomposer.states.CreatePostState

class CreatePostViewModel(
    private val userData: InMemoryUserData,
    private val clock: Clock,
    private val idGenerator: IdGenerator,
) : ViewModel() {

    private var _postState = MutableLiveData<CreatePostState>()
    val postState: LiveData<CreatePostState> = _postState

    fun createPost(postText: String) {
        if (postText == ":backend:") {
            _postState.value = CreatePostState.BackendError
        } else {
            val post = createNewPost(postText)
            _postState.value = CreatePostState.Created(post)
        }
    }

    private fun createNewPost(postText: String): Post {
        val postId = idGenerator.next()
        val userId = userData.loggedInUserId()
        val timestamp = clock.now()
        return Post(postId, userId, postText, timestamp)
    }
}
