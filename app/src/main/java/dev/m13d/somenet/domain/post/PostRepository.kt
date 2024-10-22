package dev.m13d.somenet.domain.post

import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.domain.user.UserDataStore
import dev.m13d.somenet.postcomposer.states.CreatePostState

class PostRepository(
    private val userData: UserDataStore,
    private val postsCatalog: PostsCatalog,
) {

    suspend fun createNewPost(postText: String): CreatePostState {
        return try {
            val post = postsCatalog.addPost(userData.loggedInUserId(), postText)
            CreatePostState.Created(post)
        } catch (backendException: BackendException) {
            CreatePostState.BackendError
        } catch (offlineException: ConnectionUnavailableException) {
            CreatePostState.OfflineError
        }
    }
}
