package dev.m13d.somenet.friends.states

import dev.m13d.somenet.domain.user.Following

sealed class FollowState {

    object BackendError : FollowState()

    object Offline : FollowState()

    data class Followed(val following: Following) : FollowState()

    data class Unfollowed(val following: Following) : FollowState()
}
