package dev.m13d.somenet.domain.post

data class Post(
    val id: String,
    val userId: String,
    val text: String,
    val timestamp: Long,
)
