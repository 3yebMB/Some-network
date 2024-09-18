package dev.m13d.somenet.postcomposer.states

import dev.m13d.somenet.domain.post.Post

sealed class CreatePostState {

    object BackendError : CreatePostState()

    data class Created(val post: Post): CreatePostState()

}
