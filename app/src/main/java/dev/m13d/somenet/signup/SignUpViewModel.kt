package dev.m13d.somenet.signup

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.m13d.somenet.app.CoroutineDispatchers
import dev.m13d.somenet.domain.user.UserRepository
import dev.m13d.somenet.domain.validation.CredentialsValidationResult
import dev.m13d.somenet.domain.validation.RegexCredentialValidator
import dev.m13d.somenet.signup.states.SignUpState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel(
    private val credentialValidator: RegexCredentialValidator,
    private val userRepository: UserRepository,
    private val dispatchers: CoroutineDispatchers,
): ViewModel() {
    private val _signUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState> = _signUpState

    @SuppressLint("NullSafeMutableLiveData")
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
                proceedWithSignUp(email, password, about)
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    private fun proceedWithSignUp(email: String, password: String, about: String) {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            _signUpState.value = withContext(dispatchers.background) {
                userRepository.signUp(email, password, about)
            }
        }
    }
}
