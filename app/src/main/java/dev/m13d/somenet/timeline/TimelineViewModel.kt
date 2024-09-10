package dev.m13d.somenet.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.somenet.domain.post.InMemoryPostsCatalog
import dev.m13d.somenet.domain.user.Following
import dev.m13d.somenet.timeline.states.TimelineState

class TimelineViewModel : ViewModel() {
    private var _timelineState = MutableLiveData<TimelineState>()
    val timelineState: LiveData<TimelineState> = _timelineState

    fun timelineFor(userId: String) {
        val followings = listOf(
            Following("sarahId", "lucyId"),
            Following("anabelId", "lucyId"),
        )
        val userIds = listOf(userId) + followings
            .filter { it.userId == userId }
            .map { it.followedId }
        val postsForUser = InMemoryPostsCatalog().postsFor(userIds)
        _timelineState.value = TimelineState.Posts(postsForUser)
    }
    }
}
