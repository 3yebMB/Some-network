package dev.m13d.somenet.timeline

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.m13d.somenet.app.CoroutineDispatchers
import dev.m13d.somenet.domain.timeline.TimelineRepository
import dev.m13d.somenet.timeline.states.TimelineState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimelineViewModel(
    private val timelineRepository: TimelineRepository,
    private val dispatchers: CoroutineDispatchers,
) : ViewModel() {

    private var _timelineState = MutableLiveData<TimelineState>()
    val timelineState: LiveData<TimelineState> = _timelineState

    @SuppressLint("NullSafeMutableLiveData")
    fun timelineFor(userId: String) {
        viewModelScope.launch {
            _timelineState.value = TimelineState.Loading
            _timelineState.value = withContext(dispatchers.background) {
                timelineRepository.getTimelineFor(userId)
            }
        }
    }
}
