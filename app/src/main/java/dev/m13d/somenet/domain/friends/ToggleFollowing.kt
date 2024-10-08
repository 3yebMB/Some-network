package dev.m13d.somenet.domain.friends

import dev.m13d.somenet.domain.user.Following

data class ToggleFollowing(
    val following: Following,
    val isAdded: Boolean,
)
