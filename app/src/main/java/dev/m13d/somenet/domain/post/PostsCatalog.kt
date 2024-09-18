package dev.m13d.somenet.domain.post

interface PostsCatalog {

    suspend fun postsFor(userIds: List<String>): List<Post>

    fun addPost(userId: String, postText: String): Post
}
