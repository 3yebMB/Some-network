package dev.m13d.somenet.signup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import dev.m13d.somenet.R
import dev.m13d.somenet.signup.states.SignUpScreenState
import dev.m13d.somenet.ui.component.InfoMessage
import dev.m13d.somenet.ui.component.LoadingBlock
import dev.m13d.somenet.ui.component.ScreenTitle
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    onSignedUp: (String) -> Unit,
) {
    val signUpViewModel = koinViewModel<SignUpViewModel>()
    val signUpScreenState = signUpViewModel.screenState.observeAsState().value ?: SignUpScreenState()

    if (signUpScreenState.signedUpUserId.isNotBlank()) {
        LaunchedEffect(
            key1 = signUpScreenState.signedUpUserId,
            block = { onSignedUp(signUpScreenState.signedUpUserId) }
        )
        SignUpScreenContent(
            screenState = signUpScreenState,
            onEmailChange = signUpViewModel::updateEmail,
            onPasswordChange = signUpViewModel::updatePassword,
            onAboutChange = signUpViewModel::updateAbout,
            onSignUp = signUpViewModel::createAccount
        )
    }
}

@Composable
private fun SignUpScreenContent(
    screenState: SignUpScreenState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onAboutChange: (String) -> Unit,
    onSignUp: (email: String, password: String, about: String) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ScreenTitle(R.string.createAnAccount)
            Spacer(modifier = Modifier.height(16.dp))
            val passwordFocusRequester = FocusRequester()
            val aboutFocusRequester = FocusRequester()
            EmailField(
                value = screenState.email,
                isError = screenState.isBadEmail,
                onValueChange = { onEmailChange(it) },
                onNextClicked = { passwordFocusRequester.requestFocus() }
            )
            PasswordField(
                modifier = Modifier.focusRequester(passwordFocusRequester),
                value = screenState.password,
                isError = screenState.isBadPassword,
                onValueChange = { onPasswordChange(it) },
                onNextClicked = { aboutFocusRequester.requestFocus() }
            )
            AboutField(
                modifier = Modifier.focusRequester(aboutFocusRequester),
                value = screenState.about,
                onValueChange = { onAboutChange(it) },
                onDoneClicked = { with(screenState) { onSignUp(email, password, about) } }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { with(screenState) { onSignUp(email, password, about) } }
            ) {
                Text(text = stringResource(id = R.string.signUp))
            }
        }
        InfoMessage(stringResource = screenState.error)
        LoadingBlock(screenState.isLoading)
    }
}

@Composable
private fun EmailField(
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
    onNextClicked: () -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(stringResource(id = R.string.email)),
        value = value,
        isError = isError,
        label = {
            val resource = if (isError) R.string.badEmail else R.string.email
            Text(text = stringResource(id = resource))
        },
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = { onNextClicked() })
    )
}

@Composable
private fun PasswordField(
    modifier: Modifier = Modifier,
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
    onNextClicked: () -> Unit,
) {
    var isVisible by remember { mutableStateOf(false) }
    val visualTransformation = if (isVisible) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .testTag(stringResource(id = R.string.password)),
        value = value,
        isError = isError,
        trailingIcon = {
            VisibilityToggle(isVisible) {
                isVisible = !isVisible
            }
        },
        visualTransformation = visualTransformation,
        label = {
            val resource = if (isError) R.string.badPassword else R.string.password
            Text(text = stringResource(id = resource))
        },
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(onNext = { onNextClicked() })
    )
}

@Composable
private fun VisibilityToggle(
    isVisible: Boolean,
    onToggle: () -> Unit,
) {
    IconButton(onClick = {
        onToggle()
    }) {
        val resource = if (isVisible) R.drawable.pass_visible else R.drawable.pass_invisible
        Icon(
            painter = painterResource(id = resource),
            contentDescription = stringResource(id = R.string.passwordVisibilityToggle)
        )
    }
}

@Composable
fun AboutField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onDoneClicked: () -> Unit,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        label = {
            Text(text = stringResource(id = R.string.about))
        },
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { onDoneClicked() })
    )
}
