package dev.m13d.somenet.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.domain.validation.CredentialsValidationResult
import dev.m13d.somenet.domain.validation.RegexCredentialValidator

class SignUpViewModel(
    private val credentialValidator: RegexCredentialValidator,
) {

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

            CredentialsValidationResult.Valid -> {
                val isKnown = users.values.flatten().any { it.email == email }
                if (isKnown) {
                    _signUpState.value = SignUpState.DuplicateAccount
                } else {
                    val user = createUser(email, password, about)
                    _signUpState.value = SignUpState.SignedUp(user)
                }
            }
        }
    }

    private fun createUser(email: String, password: String, about: String): User {
        val userId = email.takeWhile { it != '@' } + "Id"
        val user = User(userId = userId, email = email, about = about)
        users.getOrPut(password, ::mutableListOf).add(user)
        return user
    }

    private val users = mutableMapOf<String, MutableList<User>>()
}
