package dev.m13d.somenet.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.somenet.domain.post.InMemoryPostsCatalog
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.timeline.states.TimelineState

class TimelineViewModel(
    private val userCatalog: InMemoryUserCatalog,
    private val postCatalog: InMemoryPostsCatalog,
) : ViewModel() {
    
    private var _timelineState = MutableLiveData<TimelineState>()
    val timelineState: LiveData<TimelineState> = _timelineState

    fun timelineFor(userId: String) {
        val userIds = listOf(userId) + userCatalog.followedBy(userId)
        val postsForUser = postCatalog.postsFor(userIds)
        _timelineState.value = TimelineState.Posts(postsForUser)
    }
}
