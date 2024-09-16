package dev.m13d.somenet.timeline

import androidx.annotation.StringRes
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.m13d.somenet.R
import dev.m13d.somenet.domain.post.Post
import dev.m13d.somenet.signup.states.SignUpState
import dev.m13d.somenet.timeline.states.TimelineState
import dev.m13d.somenet.ui.component.InfoMessage
import dev.m13d.somenet.ui.component.LoadingBlock
import dev.m13d.somenet.ui.component.ScreenTitle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimelineScreenState(
    private val coroutineScope: CoroutineScope,
) {
    private var loadedUserId by mutableStateOf("")
    var posts by mutableStateOf(emptyList<Post>())
    var isLoading by mutableStateOf(false)
    var isInfoMessageShowing by mutableStateOf(false)
    var infoMessage by mutableIntStateOf(0)

    fun updatePosts(newPosts: List<Post>) {
        isLoading = false
        this.posts = newPosts
    }

    fun shouldLoadPostsFor(userId: String): Boolean {
        return if (loadedUserId != userId) {
            loadedUserId = userId
            true
        } else false
    }

    fun showLoading() {
        isLoading = true
    }

    fun showInfoMessage(@StringRes message: Int) = coroutineScope.launch {
        isLoading = false
        if (infoMessage != message) {
            infoMessage = message
            if (!isInfoMessageShowing) {
                isInfoMessageShowing = true
                delay(1500L)
                isInfoMessageShowing = false
            }
        }
    }
}

@Composable
fun TimelineScreen(
    userId: String,
    timelineViewModel: TimelineViewModel,
    onCreateNewPost: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val screenState by remember { mutableStateOf(TimelineScreenState(coroutineScope)) }
    val timelineState by timelineViewModel.timelineState.observeAsState()
    if (screenState.shouldLoadPostsFor(userId)) {
        timelineViewModel.timelineFor(userId)
    }

    when (timelineState) {
        is TimelineState.Loading -> screenState.showLoading()
        is TimelineState.Posts -> {
            val posts = (timelineState as TimelineState.Posts).posts
            screenState.updatePosts(posts)
        }
        is TimelineState.BackendError -> screenState.showInfoMessage(R.string.fetchingTimelineError)
        is TimelineState.OfflineError -> screenState.showInfoMessage(R.string.offlineError)
        else -> {}
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            ScreenTitle(titleResource = R.string.timeline)
            Spacer(Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxSize()) {
                PostsList(
                    screenState.posts,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
                FloatingActionButton(
                    onClick = { onCreateNewPost() },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .testTag(stringResource(id = R.string.createNewPost))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.createNewPost),
                    )
                }
            }
        }
        InfoMessage(
            isVisible = screenState.isInfoMessageShowing,
            stringResource = screenState.infoMessage,
        )
        LoadingBlock(screenState.isLoading)
    }
}

@Composable
private fun PostsList(
    posts: List<Post>,
    modifier: Modifier = Modifier,
) {
    if (posts.isEmpty()) {
        Text(
            text = stringResource(id = R.string.emptyTimelineMessage),
            modifier = modifier,
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
        Text(
            text = post.text,
            modifier = Modifier.padding(8.dp)
        )
    }
}
