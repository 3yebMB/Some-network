package dev.m13d.somenet.postcomposer

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.m13d.somenet.R
import dev.m13d.somenet.postcomposer.states.CreatePostState
import dev.m13d.somenet.ui.component.InfoMessage
import dev.m13d.somenet.ui.component.LoadingBlock
import dev.m13d.somenet.ui.component.ScreenTitle
import org.koin.androidx.compose.koinViewModel

class CreateNewPostScreenState {
    var isPostSubmitted by mutableStateOf(false)
    var currentMessage by mutableIntStateOf(0)
    var isLoading by mutableStateOf(false)

    fun setPostSubmitted() {
        isPostSubmitted = true
    }

    fun showMessage(@StringRes message: Int) {
        isLoading = false
        if (currentMessage != message)
            currentMessage = message
    }

    fun showLoading() {
        isLoading = true
    }
}

@Composable
fun CreateNewPostScreen(
    onPostCreated: () -> Unit,
) {
    val createPostViewModel = koinViewModel<CreatePostViewModel>()
    val screenState by remember { mutableStateOf(CreateNewPostScreenState()) }
    var postText by remember { mutableStateOf("") }

    val createPostState by createPostViewModel.postState.observeAsState()
    when (createPostState) {
        is CreatePostState.Loading ->
            screenState.showLoading()

        is CreatePostState.Created -> {
            if (screenState.isPostSubmitted) {
                onPostCreated()
            }
        }

        is CreatePostState.BackendError ->
            screenState.showMessage(R.string.creatingPostError)

        is CreatePostState.OfflineError ->
            screenState.showMessage(R.string.offlineError)

        else -> {}
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ScreenTitle(titleResource = R.string.createNewPost)
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxSize()) {
                PostComposer(postText) { postText = it }
                FloatingActionButton(
                    onClick = {
                        screenState.setPostSubmitted()
                        createPostViewModel.createPost(postText)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .testTag(stringResource(R.string.submitPost))
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = stringResource(id = R.string.submitPost)
                    )
                }
            }
        }
        InfoMessage(stringResource = screenState.currentMessage)
        LoadingBlock(isShowing = screenState.isLoading)
    }
}

@Composable
private fun PostComposer(
    postText: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = postText,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = stringResource(id = R.string.newPostHint)) }
    )
}
