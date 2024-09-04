package dev.m13d.somenet.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.regex.Pattern

class SignUpViewModel {

    private val _signUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> = _signUpState

    fun createAccount(
        email: String,
        password: String,
        about: String
    ) {
        val emailRegEx = """[a-zA-Z0-9+._%\-]{1,64}@[a-zA-Z0-9][a-zA-Z0-9\-]{1,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{1,25})"""
        val emailPattern = Pattern.compile(emailRegEx)
        if (!emailPattern.matcher(email).matches()) {
            _signUpState.value = SignUpState.BadEmail
        } else if (password == "") {
            _signUpState.value = SignUpState.BadPassword
        }
    }
}
