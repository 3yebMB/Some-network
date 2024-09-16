package dev.m13d.somenet.timeline.states

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.m13d.somenet.domain.post.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimelineScreenState(
    private val coroutineScope: CoroutineScope,
) {
    private var loadedUserId by mutableStateOf("")
    var posts by mutableStateOf(emptyList<Post>())
    var isLoading by mutableStateOf(false)
    var isInfoMessageShowing by mutableStateOf(false)
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

    fun showInfoMessage(@StringRes message: Int) = coroutineScope.launch {
        isLoading = false
        if (infoMessage != message) {
            infoMessage = message
            if (!isInfoMessageShowing) {
                isInfoMessageShowing = true
                delay(1500L)
                isInfoMessageShowing = false
            }
        }
    }
}
