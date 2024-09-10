package dev.m13d.somenet.domain.user

data class Following(
    val userId: String,
    val followedId: String,
)
