package dev.m13d.somenet.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
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
        _postState.value = createNewPost(postText)
    }

    private fun createNewPost(postText: String): CreatePostState {
        return try {
            val post = addPost(userData.loggedInUserId(), postText)
            CreatePostState.Created(post)
        } catch (backendException: BackendException) {
            CreatePostState.BackendError
        } catch (offlineException: ConnectionUnavailableException) {
            CreatePostState.OfflineError
        }
    }

    @Suppress("ThrowableNotThrown")
    private fun addPost(userId: String, postText: String): Post {
        if (postText == ":backend:") {
            BackendException()
        } else if (postText == ":offline:") {
            ConnectionUnavailableException()
        }
        val postId = idGenerator.next()
        val timestamp = clock.now()
        return Post(postId, userId, postText, timestamp)

    }
}
