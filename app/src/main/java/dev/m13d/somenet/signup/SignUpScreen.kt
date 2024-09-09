package dev.m13d.somenet.signup

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import dev.m13d.somenet.R
import dev.m13d.somenet.signup.states.SignUpScreenState
import dev.m13d.somenet.signup.states.SignUpState

@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel,
    onSignedUp: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val screenState by remember { mutableStateOf(
        SignUpScreenState(coroutineScope)
    ) }
    val signUpState by signUpViewModel.signUpState.observeAsState()

    when (signUpState) {
        is SignUpState.SignedUp -> onSignedUp()
        is SignUpState.BadEmail -> screenState.isBadEmail = true
        is SignUpState.BadPassword -> screenState.isBadPassword = true
        is SignUpState.DuplicateAccount -> screenState.toggleInfoMessage(R.string.duplicateAccountError)
        is SignUpState.BackendError -> screenState.toggleInfoMessage(R.string.createAccountError)
        is SignUpState.Offline -> screenState.toggleInfoMessage(R.string.offlineError)
        else -> {}
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ScreenTitle(R.string.createAnAccount)
            Spacer(Modifier.height(8.dp))
            LoginField(
                value = screenState.email,
                isError = screenState.isBadEmail,
                onValueChange = { screenState.email = it },
            )
            PasswordField(
                value = screenState.pass,
                isError = screenState.isBadPassword,
                onValueChange = { screenState.pass = it },
            )
            Spacer(Modifier.height(8.dp))
            AboutField(
                value = screenState.about,
                onValueChange = { screenState.about = it },
            )
            Spacer(Modifier.height(8.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    screenState.resetUiState()
                    with(screenState) {
                        signUpViewModel.createAccount(email, pass, about)
                    }
                }
            ) {
                Text(text = stringResource(id = R.string.signUp))
            }
        }
        screenState.infoMessage?.let {
            InfoMessage(isVisible = screenState.isInfoMessageShowing, stringResource = it)
        }
    }
}

@Composable
fun InfoMessage(
    isVisible: Boolean,
    @StringRes stringResource: Int,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 150, easing = FastOutLinearInEasing)
        ),
        exit = fadeOut(
            targetAlpha = 0f,
            animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
        ),
    ) {
        Surface(
            color = MaterialTheme.colorScheme.error,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(id = stringResource),
                    color = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun ScreenTitle(@StringRes titleResource: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = titleResource),
            style = typography.headlineMedium
        )
    }
}

@Composable
private fun LoginField(
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        isError = isError,
        label = {
            val resource = if (isError) R.string.badEmail else R.string.email
            Text(text = stringResource(id = resource))
        },
        onValueChange = onValueChange,
    )
}

@Composable
private fun PasswordField(
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
) {
    var isPassVisible by remember { mutableStateOf(false) }
    val visualTransformation = if (isPassVisible) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }
    OutlinedTextField(
        modifier = Modifier
            .testTag(stringResource(id = R.string.password))
            .fillMaxWidth(),
        value = value,
        isError = isPassVisible,
        trailingIcon = {
            VisibilityToggle(isPassVisible) {
                isPassVisible = !isPassVisible
            }
        },
        visualTransformation = visualTransformation,
        label = {
            val resource = if (isError) R.string.badPassword else R.string.password
            Text(text = stringResource(resource))
        },
        onValueChange = onValueChange,
    )
}


@Composable
private fun VisibilityToggle(
    isPassVisible: Boolean,
    onToggle: () -> Unit,
) {
    IconButton(onClick = { onToggle() }) {
        val resource = if (isPassVisible) R.drawable.pass_visible else R.drawable.pass_invisible
        Icon(
            painter = painterResource(id = resource),
            contentDescription = stringResource(id = R.string.passwordVisibilityToggle)
        )
    }
}

@Composable
private fun AboutField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        label = { Text(text = stringResource(id = R.string.about)) },
        onValueChange = onValueChange,
    )
}
