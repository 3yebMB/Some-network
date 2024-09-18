package dev.m13d.somenet.postcomposer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.somenet.domain.post.PostRepository
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
        val result = PostRepository(userData, clock, idGenerator).createNewPost(postText)
        _postState.value = result
    }
}
