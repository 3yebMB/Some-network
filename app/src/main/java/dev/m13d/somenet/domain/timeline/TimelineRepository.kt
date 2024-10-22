package dev.m13d.somenet.domain.timeline

import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.domain.post.PostsCatalog
import dev.m13d.somenet.domain.user.UserCatalog
import dev.m13d.somenet.timeline.states.TimelineState

class TimelineRepository(
    private val userCatalog: UserCatalog,
    private val postCatalog: PostsCatalog,
) {
    suspend fun getTimelineFor(userId: String): TimelineState {
        return try {
            val userIds = listOf(userId) + userCatalog.followedBy(userId)
            val postsForUser = postCatalog.postsFor(userIds)
            TimelineState.Posts(postsForUser)
        } catch (backendException: BackendException) {
            TimelineState.BackendError
        } catch (offlineException: ConnectionUnavailableException) {
            TimelineState.OfflineError
        }
    }
}
