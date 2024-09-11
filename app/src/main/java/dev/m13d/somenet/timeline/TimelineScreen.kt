package dev.m13d.somenet.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.m13d.somenet.R
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.timeline.states.TimelineState
import dev.m13d.somenet.ui.component.ScreenTitle

class TimelineScreenState {
    var posts by mutableStateOf(emptyList<Post>())

    fun updatePosts(newPosts: List<Post>) {
        this.posts = newPosts
    }

}

@Composable
fun TimelineScreen(
    userId: String,
    timelineViewModel: TimelineViewModel,
) {
    val screenState by remember { mutableStateOf(TimelineScreenState()) }
    val timelineState by timelineViewModel.timelineState.observeAsState()
    timelineViewModel.timelineFor(userId)

    if (timelineState is TimelineState.Posts) {
        val posts = (timelineState as TimelineState.Posts).posts
        screenState.updatePosts(posts)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        ScreenTitle(titleResource = R.string.timeline)
        PostsList(screenState.posts)
    }
}

@Composable
private fun PostsList(posts: List<Post>) {
    if (posts.isEmpty()) {
        Text(text = stringResource(id = R.string.emptyTimelineMessage))
    } else {
        LazyColumn {
            items(posts) { post ->
                PostItem(post)
            }
        }
    }
}

@Composable
private fun PostItem(post: Post) {
    Text(text = post.text)
}
