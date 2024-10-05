package dev.m13d.somenet.signup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import dev.m13d.somenet.ui.component.InfoMessage
import dev.m13d.somenet.ui.component.LoadingBlock
import dev.m13d.somenet.ui.component.ScreenTitle
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    onSignedUp: (String) -> Unit,
) {
    val signUpViewModel = koinViewModel<SignUpViewModel>()
    val screenState by remember { mutableStateOf(SignUpScreenState()) }
    val signUpState by signUpViewModel.signUpState.observeAsState()

    when (signUpState) {
        is SignUpState.Loading -> screenState.toggleLoading()
        is SignUpState.SignedUp -> onSignedUp((signUpState as SignUpState.SignedUp).user.id)
        is SignUpState.BadEmail -> screenState.showBadEmail()
        is SignUpState.BadPassword -> screenState.showBadPassword()
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
                isError = screenState.showBadEmail,
                onValueChange = { screenState.email = it },
            )
            PasswordField(
                value = screenState.password,
                isError = screenState.showBadPassword,
                onValueChange = { screenState.password = it },
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
                        signUpViewModel.createAccount(email, password, about)
                    }
                }
            ) {
                Text(text = stringResource(id = R.string.signUp))
            }
        }
        InfoMessage(
            stringResource = screenState.infoMessage,
        )
        LoadingBlock(screenState.isLoading)
    }
}

@Composable
private fun LoginField(
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .testTag(stringResource(R.string.email))
            .fillMaxWidth(),
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
        isError = isError,
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
