package dev.m13d.somenet.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.m13d.somenet.domain.user.UserRepository
import dev.m13d.somenet.domain.validation.CredentialsValidationResult
import dev.m13d.somenet.domain.validation.RegexCredentialValidator
import dev.m13d.somenet.signup.states.SignUpState

class SignUpViewModel(
    private val credentialValidator: RegexCredentialValidator,
    private val userRepository: UserRepository,
): ViewModel() {
    private val _signUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> = _signUpState

    fun createAccount(
        email: String,
        password: String,
        about: String
    ) {
        when (credentialValidator.validate(email, password)) {
            CredentialsValidationResult.InvalidEmail ->
                _signUpState.value = SignUpState.BadEmail

            CredentialsValidationResult.InvalidPassword ->
                _signUpState.value = SignUpState.BadPassword

            CredentialsValidationResult.Valid ->
                _signUpState.value = userRepository.signUp(email, password, about)
        }
    }
}
