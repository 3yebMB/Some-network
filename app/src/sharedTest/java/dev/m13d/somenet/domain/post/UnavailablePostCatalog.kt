package dev.m13d.somenet.domain.post

import dev.m13d.somenet.domain.exceptions.BackendException

class UnavailablePostCatalog : PostsCatalog {
    override suspend fun postsFor(userIds: List<String>): List<Post> {
        throw BackendException()
    }

    override suspend fun addPost(userId: String, postText: String): Post {
        throw BackendException()
    }
}
