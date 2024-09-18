package dev.m13d.somenet.domain.post

import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.domain.user.InMemoryUserData
import dev.m13d.somenet.infrastructure.Clock
import dev.m13d.somenet.infrastructure.IdGenerator
import dev.m13d.somenet.postcomposer.states.CreatePostState

class PostRepository(
    private val userData: InMemoryUserData,
    private val clock: Clock,
    private val idGenerator: IdGenerator,
) {

    internal fun createNewPost(postText: String): CreatePostState {
        return try {
            val post = InMemoryPostsCatalog(
                idGenerator = idGenerator,
                clock = clock,
            ).addPost(userData.loggedInUserId(), postText)
            CreatePostState.Created(post)
        } catch (backendException: BackendException) {
            CreatePostState.BackendError
        } catch (offlineException: ConnectionUnavailableException) {
            CreatePostState.OfflineError
        }
    }
}
