package dev.m13d.somenet.postcomposer.states

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CreateNewPostScreenStateOld {
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
