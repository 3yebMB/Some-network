package dev.m13d.somenet.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.timeline.states.TimelineState

class TimelineViewModel : ViewModel() {
    private var _timelineState = MutableLiveData<TimelineState>()
    val timelineState: LiveData<TimelineState> = _timelineState

    fun timelineFor(userId: String) {
        val posts = when (userId) {
            "anabelId" -> listOf(
                Post("post2", "lucyId", "post 2", 2L),
                Post("post1", "lucyId", "post 1", 1L)
            )
            "timId" -> listOf(Post("postId", "timId", "post text", 1L))
            else -> emptyList()
        }
        _timelineState.value = TimelineState.Posts(posts)
    }
}
