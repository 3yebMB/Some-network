package dev.m13d.somenet.postcomposer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import dev.m13d.somenet.R
import dev.m13d.somenet.postcomposer.states.CreatePostState
import dev.m13d.somenet.ui.component.ScreenTitle

@Composable
fun CreateNewPostScreen(
    createPostViewModel: CreatePostViewModel,
    onPostCreated: () -> Unit,
) {

    var postText by remember { mutableStateOf("") }

    val createPostState by createPostViewModel.postState.observeAsState()
    when(createPostState) {
        is CreatePostState.Created -> onPostCreated()
        else -> {}
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ScreenTitle(titleResource = R.string.createNewPost)
        Box(modifier = Modifier.fillMaxSize()) {
            PostComposer(postText) { postText = it }
            FloatingActionButton(
                onClick = { createPostViewModel.createPost(postText) },
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
