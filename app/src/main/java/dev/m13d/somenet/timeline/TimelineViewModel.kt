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
        val userIds = listOf(userId) + followedBy(userId)
        val postsForUser = InMemoryPostsCatalog().postsFor(userIds)
        _timelineState.value = TimelineState.Posts(postsForUser)
    }

    private fun followedBy(userId: String): List<String> {
        val followings = listOf(
            Following("sarahId", "lucyId"),
            Following("anabelId", "lucyId"),
        )
        return followings
            .filter { it.userId == userId }
            .map { it.followedId }
    }
}
