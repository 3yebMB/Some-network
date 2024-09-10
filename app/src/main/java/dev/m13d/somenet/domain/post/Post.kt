package dev.m13d.somenet.domain.post

data class Post(
    val postId: String,
    val userId: String,
    val postText: String,
    val timestamp: Long,
)
