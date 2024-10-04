package dev.m13d.somenet.domain.user

data class Friend(
    val user: User,
    val isFollower: Boolean,
)
