package dev.m13d.somenet.timeline.states

import dev.m13d.somenet.domain.post.Post

sealed class TimelineState {

    object BackendError : TimelineState()

    data class Posts(val posts: List<Post>) : TimelineState()

}
