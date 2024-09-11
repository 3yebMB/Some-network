package dev.m13d.somenet.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.somenet.domain.post.PostsCatalog
import dev.m13d.somenet.domain.timeline.TimelineRepository
import dev.m13d.somenet.domain.user.UserCatalog
import dev.m13d.somenet.timeline.states.TimelineState

class TimelineViewModel(
    private val userCatalog: UserCatalog,
    private val postCatalog: PostsCatalog,
) : ViewModel() {

    private var _timelineState = MutableLiveData<TimelineState>()
    val timelineState: LiveData<TimelineState> = _timelineState

    fun timelineFor(userId: String) {
        val result = TimelineRepository(userCatalog, postCatalog)
            .getTimelineFor(userId)
        _timelineState.value = result
    }
}
