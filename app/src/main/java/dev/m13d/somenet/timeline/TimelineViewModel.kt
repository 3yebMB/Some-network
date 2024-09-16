package dev.m13d.somenet.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.somenet.domain.timeline.TimelineRepository
import dev.m13d.somenet.timeline.states.TimelineState

class TimelineViewModel (
    private val timelineRepository: TimelineRepository,
) : ViewModel() {

    private var _timelineState = MutableLiveData<TimelineState>()
    val timelineState: LiveData<TimelineState> = _timelineState

    fun timelineFor(userId: String) {
        _timelineState.value = TimelineState.Loading
        val result = timelineRepository.getTimelineFor(userId)
        _timelineState.value = result
    }
}
