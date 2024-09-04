package dev.m13d.somenet.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SignUpViewModel {

    private val _signUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> = _signUpState

    fun createAccount(
        email: String,
        password: String,
        about: String
    ) {
        _signUpState.value = SignUpState.BadEmail
    }
}
