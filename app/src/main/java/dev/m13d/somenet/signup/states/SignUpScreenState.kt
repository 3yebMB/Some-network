package dev.m13d.somenet.signup.states

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUpScreenState(
    private val coroutineScope: CoroutineScope,
) {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isBadEmail by mutableStateOf(false)
    var isBadPassword by mutableStateOf(false)
    var about by mutableStateOf("")
    var infoMessage by mutableIntStateOf(0)
    var isInfoMessageShowing by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    private var lastSubmittedEmail by mutableStateOf("")
    val showBadEmail: Boolean
        get() = isBadEmail && lastSubmittedEmail == email

    private var lastSubmittedPassword by mutableStateOf("")
    val showBadPassword: Boolean
        get() = isBadPassword && lastSubmittedPassword == password

    fun showBadEmail() {
        isBadEmail = true
    }

    fun showBadPassword() {
        isBadPassword = true
    }

    fun toggleInfoMessage(@StringRes message: Int) = coroutineScope.launch {
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

    fun toggleLoading() {
        isLoading = true
    }

    fun resetUiState() {
        infoMessage = 0
        lastSubmittedEmail = email
        lastSubmittedPassword = password
        isInfoMessageShowing = false
        isBadEmail = false
        isBadPassword = false
        isLoading = false
    }
}
