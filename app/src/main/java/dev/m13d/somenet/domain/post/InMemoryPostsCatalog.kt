package dev.m13d.somenet.domain.post

import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.infrastructure.Clock
import dev.m13d.somenet.infrastructure.IdGenerator
import dev.m13d.somenet.infrastructure.SystemClock
import dev.m13d.somenet.infrastructure.UUIDGenerator

class InMemoryPostsCatalog(
    private val availablePosts: List<Post> = emptyList(),
    private val idGenerator: IdGenerator = UUIDGenerator(),
    private val clock: Clock = SystemClock(),
) : PostsCatalog {

    override suspend fun postsFor(userIds: List<String>): List<Post> {
        return availablePosts.filter { userIds.contains(it.userId) }
    }

    override suspend fun addPost(userId: String, postText: String): Post {
        val postId = idGenerator.next()
        val timestamp = clock.now()
        return Post(postId, userId, postText, timestamp)
    }
}
