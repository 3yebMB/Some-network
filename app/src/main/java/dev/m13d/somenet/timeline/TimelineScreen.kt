package dev.m13d.somenet.timeline

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
                PostItem(post = post)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun PostItem(
    post: Post,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .clip(shape = RoundedCornerShape(16.dp))
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface,
            shape = RoundedCornerShape(16.dp)
        )
    ) {
        Text(
            text = post.text,
            modifier = Modifier.padding(8.dp)
        )
    }
}
