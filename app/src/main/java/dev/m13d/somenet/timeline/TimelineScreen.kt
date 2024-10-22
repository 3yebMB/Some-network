package dev.m13d.somenet.timeline

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.m13d.somenet.R
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.timeline.states.TimelineScreenState
import dev.m13d.somenet.ui.component.InfoMessage
import dev.m13d.somenet.ui.component.PullToRefreshBox
import dev.m13d.somenet.ui.component.ScreenTitle
import dev.m13d.somenet.ui.extentions.toDateTime
import org.koin.androidx.compose.koinViewModel

@Composable
fun TimelineScreen(
    userId: String,
    onCreateNewPost: () -> Unit,
) {
    val timelineViewModel = koinViewModel<TimelineViewModel>()
    val screenState = timelineViewModel.screenState.observeAsState().value ?: TimelineScreenState()

    LaunchedEffect(key1 = userId, block = { timelineViewModel.timelineFor(userId) })
    TimelineScreenContent(
        screenState = screenState,
        onCreateNewPost = { onCreateNewPost() },
        onRefresh = { timelineViewModel.timelineFor(userId) }
    )
}

@Composable
private fun TimelineScreenContent(
    modifier: Modifier = Modifier,
    screenState: TimelineScreenState,
    onCreateNewPost: () -> Unit,
    onRefresh: () -> Unit,
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            ScreenTitle(titleResource = R.string.timeline)
            Spacer(Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxSize()) {
                PostsList(
                    isRefreshing = screenState.isLoading,
                    posts = screenState.posts,
                    onRefresh = { onRefresh() },
                )
                FloatingActionButton(
                    onClick = { onCreateNewPost() },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .testTag(stringResource(id = R.string.createNewPost))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.createNewPost),
                    )
                }
            }
        }
        InfoMessage(stringResource = screenState.error)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostsList(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    posts: List<Post>,
    onRefresh: () -> Unit,
) {
    val loadingContentDescription = stringResource(id = R.string.loading)

    PullToRefreshBox(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = loadingContentDescription },
        isRefreshing = isRefreshing,
        onRefresh = { onRefresh() },
    ) {
        if (posts.isEmpty()) {
            Text(
                text = stringResource(id = R.string.emptyTimelineMessage),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(posts) { post ->
                    PostItem(post = post)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun PostItem(
    post: Post,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = post.userId)
                Text(text = post.timestamp.toDateTime())
            }
            Text(
                text = post.text,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
