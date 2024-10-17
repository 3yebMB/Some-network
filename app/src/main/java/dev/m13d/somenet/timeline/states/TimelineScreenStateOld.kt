package dev.m13d.somenet.timeline.states

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.m13d.somenet.domain.post.Post

class TimelineScreenStateOld {
    private var loadedUserId by mutableStateOf("")
    var posts by mutableStateOf(emptyList<Post>())
    var isLoading by mutableStateOf(false)
    var infoMessage by mutableIntStateOf(0)

    fun updatePosts(newPosts: List<Post>) {
        isLoading = false
        this.posts = newPosts
    }

    fun shouldLoadPostsFor(userId: String): Boolean {
        return if (loadedUserId != userId) {
            loadedUserId = userId
            true
        } else false
    }

    fun showLoading() {
        isLoading = true
    }

    fun showInfoMessage(@StringRes message: Int)  {
        isLoading = false
        if (infoMessage != message) {
            infoMessage = message
        }
    }
}
