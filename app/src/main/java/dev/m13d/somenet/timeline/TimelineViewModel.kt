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
        val availablePosts = listOf(
            Post("post2", "lucyId", "post 2", 2L),
            Post("post1", "lucyId", "post 1", 1L),
            Post("postId", "timId", "post text", 1L),
            Post("post4", "sarahId", "post 4", 4L),
            Post("post3", "sarahId", "post 3", 3L),
        )

        val posts = when (userId) {
            "sarahId" -> availablePosts.filter { it.userId == "lucyId" || it.userId == "sarahId" }
            "anabelId" -> availablePosts.filter { it.userId == "lucyId" }
            "timId" -> availablePosts.filter { it.userId == "timId" }
            else -> emptyList()
        }
        _timelineState.value = TimelineState.Posts(posts)
    }
}
