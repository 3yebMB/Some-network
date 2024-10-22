package dev.m13d.somenet.postcomposer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.m13d.somenet.R
import dev.m13d.somenet.postcomposer.states.CreateNewPostScreenState
import dev.m13d.somenet.postcomposer.states.CreateNewPostScreenStateOld
import dev.m13d.somenet.postcomposer.states.CreatePostState
import dev.m13d.somenet.ui.component.InfoMessage
import dev.m13d.somenet.ui.component.LoadingBlock
import dev.m13d.somenet.ui.component.ScreenTitle
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateNewPostScreen(
    onPostCreated: () -> Unit,
) {
    val createPostViewModel = koinViewModel<CreatePostViewModel>()
    val createPostState = createPostViewModel.screenState.observeAsState().value ?: CreateNewPostScreenState()

    if (createPostState.createdPostId.isNotBlank()) {
        onPostCreated()
    }

    CreateNewPostScreenContent(
        screenState = createPostState,
        onPostTextUpdated = createPostViewModel::updatePostText,
        onSubmitPost = { createPostViewModel.createPost(it) },
    )
}

@Composable
private fun CreateNewPostScreenContent(
    screenState: CreateNewPostScreenState,
    onPostTextUpdated: (String) -> Unit,
    onSubmitPost: (String) -> Unit,
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ScreenTitle(titleResource = R.string.createNewPost)
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxSize()) {
                PostComposer(
                    postText = screenState.postText,
                    onValueChange = { onPostTextUpdated(it) },
                    onDone = { onSubmitPost(screenState.postText) },
                )
                FloatingActionButton(
                    onClick = { onSubmitPost(screenState.postText) },
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
        InfoMessage(stringResource = screenState.error)
        LoadingBlock(isShowing = screenState.isLoading)
    }
}

@Composable
private fun PostComposer(
    postText: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
) {
    OutlinedTextField(
        value = postText,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = stringResource(id = R.string.newPostHint)) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
    )
}
