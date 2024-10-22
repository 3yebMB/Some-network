package dev.m13d.somenet.domain.post

import kotlinx.coroutines.delay

class DelayingPostCatalog : PostsCatalog {

    override suspend fun postsFor(userIds: List<String>): List<Post> {
        delay(3500L)
        return emptyList()
    }

    override suspend fun addPost(userId: String, postText: String): Post {
        delay(3500L)
        return Post("postId", userId, postText, 0)
    }
}
