package dev.m13d.somenet.timeline

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.m13d.somenet.R
import dev.m13d.somenet.app.CoroutineDispatchers
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.domain.timeline.TimelineRepository
import dev.m13d.somenet.timeline.states.TimelineScreenState
import dev.m13d.somenet.timeline.states.TimelineState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimelineViewModel(
    private val timelineRepository: TimelineRepository,
    private val savedStateHandle: SavedStateHandle,
    private val dispatchers: CoroutineDispatchers,
) : ViewModel() {

    private companion object {
        private const val SCREEN_STATE_KEY = "timelineScreenState"
    }

    val timelineScreenState: LiveData<TimelineScreenState> = savedStateHandle.getLiveData(SCREEN_STATE_KEY)

    @SuppressLint("NullSafeMutableLiveData")
    fun timelineFor(userId: String) {
        viewModelScope.launch {
            setLoading()
            val result = withContext(dispatchers.background) {
                timelineRepository.getTimelineFor(userId)
            }
            updateScreenStateFor(result)
        }
    }

    private fun updateScreenStateFor(timelineState: TimelineState) {
        when (timelineState) {
            is TimelineState.Posts -> setPosts(timelineState.posts)
            is TimelineState.BackendError -> setError(R.string.fetchingTimelineError)
            is TimelineState.OfflineError -> setError(R.string.offlineError)
            else -> {}
        }
    }

    private fun setLoading() {
        val screenState = currentScreenState()
        updateScreenState(screenState.copy(isLoading = true))
    }

    private fun setPosts(posts: List<Post>) {
        val screenState = currentScreenState()
        updateScreenState(screenState.copy(isLoading = false, posts = posts))
    }

    private fun setError(@StringRes errorResource: Int) {
        val screenState = currentScreenState()
        updateScreenState(screenState.copy(isLoading = false, error = errorResource))
    }

    private fun currentScreenState(): TimelineScreenState {
        return savedStateHandle[SCREEN_STATE_KEY] ?: TimelineScreenState()
    }

    private fun updateScreenState(newState: TimelineScreenState) {
        savedStateHandle[SCREEN_STATE_KEY] = newState
    }
}
