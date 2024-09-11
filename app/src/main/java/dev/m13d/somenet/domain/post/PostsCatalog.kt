package dev.m13d.somenet.domain.post

interface PostsCatalog {

    fun postsFor(userIds: List<String>): List<Post>
}
