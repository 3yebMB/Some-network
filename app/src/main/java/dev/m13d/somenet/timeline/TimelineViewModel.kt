package dev.m13d.somenet.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.m13d.somenet.app.CoroutineDispatchers
import dev.m13d.somenet.domain.timeline.TimelineRepository
import dev.m13d.somenet.timeline.states.TimelineState
import kotlinx.coroutines.launch

class TimelineViewModel(
    private val timelineRepository: TimelineRepository,
    private val dispatchers: CoroutineDispatchers,
) : ViewModel() {

    private var _timelineState = MutableLiveData<TimelineState>()
    val timelineState: LiveData<TimelineState> = _timelineState

    fun timelineFor(userId: String) {
        viewModelScope.launch {
            _timelineState.value = TimelineState.Loading
            val result = timelineRepository.getTimelineFor(userId)
            _timelineState.value = result
        }
    }
}
