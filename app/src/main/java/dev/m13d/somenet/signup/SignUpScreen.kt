package dev.m13d.somenet.signup

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme.typography
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.m13d.somenet.R
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.domain.user.UserRepository
import dev.m13d.somenet.domain.validation.RegexCredentialValidator

@Composable
fun SignUpScreen(
    onSignedUp: () -> Unit,
) {

    val credentialValidator = RegexCredentialValidator()
    val userRepository = UserRepository(InMemoryUserCatalog())
    val signUpViewModel = SignUpViewModel(credentialValidator, userRepository)

    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    val signUpState by signUpViewModel.signUpState.observeAsState()

    if (signUpState is SignUpState.SignedUp) {
        onSignedUp()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScreenTitle(R.string.createAnAccount)
        Spacer(Modifier.height(8.dp))
        LoginField(
            value = email,
            onValueChange = { email = it }
        )
        PasswordField(
            value = pass,
            onValueChange = { pass = it }
        )
        Spacer(Modifier.height(8.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                signUpViewModel.createAccount(email, pass, "")
            }
        ) {
            Text(text = stringResource(id = R.string.signUp))
        }
    }
}

@Composable
private fun PasswordField(
    value: String,
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
        trailingIcon = {
            VisibilityToggle(isPassVisible) {
                isPassVisible = !isPassVisible
            }
        },
        visualTransformation = visualTransformation,
        label = { Text(text = stringResource(id = R.string.password)) },
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
            contentDescription = stringResource(id = R.string.passwordToggleVisiblity)
        )
    }
}

@Composable
private fun LoginField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        label = { Text(text = stringResource(id = R.string.email)) },
        onValueChange = onValueChange,
    )
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
@Preview(device = "id:pixel_8", showBackground = true)
fun SignUpScreen() {
    val onSignedUp: () -> Unit
    SignUpScreen {}
}
