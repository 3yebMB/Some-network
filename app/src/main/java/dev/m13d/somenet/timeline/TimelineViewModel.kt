package dev.m13d.somenet.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.domain.post.PostsCatalog
import dev.m13d.somenet.domain.user.UserCatalog
import dev.m13d.somenet.timeline.states.TimelineState

class TimelineViewModel(
    private val userCatalog: UserCatalog,
    private val postCatalog: PostsCatalog,
) : ViewModel() {

    private var _timelineState = MutableLiveData<TimelineState>()
    val timelineState: LiveData<TimelineState> = _timelineState

    fun timelineFor(userId: String) {
        try {
            val userIds = listOf(userId) + userCatalog.followedBy(userId)
            val postsForUser = postCatalog.postsFor(userIds)
            _timelineState.value = TimelineState.Posts(postsForUser)
        } catch (e: BackendException) {
            _timelineState.value = TimelineState.BackendError
        } catch (e: ConnectionUnavailableException) {
            _timelineState.value = TimelineState.OfflineError
        }    
    }
}
