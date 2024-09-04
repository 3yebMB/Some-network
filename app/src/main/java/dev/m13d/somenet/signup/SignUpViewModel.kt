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
        when (validate(email, password)) {
            CredentialsValidationResult.InvalidEmail ->
                _signUpState.value = SignUpState.BadEmail

            CredentialsValidationResult.InvalidPassword ->
                _signUpState.value = SignUpState.BadPassword
        }
    }

    private fun validate(
        email: String,
        password: String
    ): CredentialsValidationResult {
        val emailRegEx = """[a-zA-Z0-9+._%\-]{1,64}@[a-zA-Z0-9][a-zA-Z0-9\-]{1,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{1,25})"""
        val emailPattern = Pattern.compile(emailRegEx)

        val passwordRegEx = """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=\-]).{8,}$"""
        val passwordPattern = Pattern.compile(passwordRegEx)

        val result = if (!emailPattern.matcher(email).matches()) {
            CredentialsValidationResult.InvalidEmail
        } else if (!passwordPattern.matcher(password).matches()) {
            CredentialsValidationResult.InvalidPassword
        } else TODO("Not implemented yet")
        return result
    }
}

sealed class CredentialsValidationResult {

    object InvalidEmail : CredentialsValidationResult()

    object InvalidPassword : CredentialsValidationResult()
}
