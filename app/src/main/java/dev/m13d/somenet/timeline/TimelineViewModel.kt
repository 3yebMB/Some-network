package dev.m13d.somenet.timeline

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.somenet.timeline.states.TimelineState

class TimelineViewModel: ViewModel() {
    private var _timelineState = MutableLiveData<TimelineState>()
    val timelineState: LiveData<TimelineState> = _timelineState

    fun timelineFor(userId: String) {
        _timelineState.value = TimelineState.Posts(emptyList())
    }
}
