package dev.m13d.somenet.domain.post

import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException

class OfflinePostCatalog : PostsCatalog {
    override suspend fun postsFor(userIds: List<String>): List<Post> {
        throw ConnectionUnavailableException()
    }

    override fun addPost(userId: String, postText: String): Post {
        throw ConnectionUnavailableException()
    }
}
