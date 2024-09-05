package dev.m13d.somenet.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.domain.validation.CredentialsValidationResult
import dev.m13d.somenet.domain.validation.RegexCredentialValidator

typealias PasswordUserType = MutableMap<String, MutableList<User>>

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

            CredentialsValidationResult.Valid ->
                _signUpState.value = signUpState(email, password, about)
        }
    }

    private val userCatalog = InMemoryUserCatalog()

    private fun signUpState(
        email: String,
        password: String,
        about: String
    ): SignUpState {
        return try {
            val user = userCatalog.createUser(email, password, about)
            SignUpState.SignedUp(user)
        } catch (duplicateAccount: DuplicateAccountException) {
            SignUpState.DuplicateAccount
        }
    }

    class InMemoryUserCatalog(
        private val users: PasswordUserType = mutableMapOf()
    ) {
        fun createUser(
            email: String,
            password: String,
            about: String
        ): User {
            checkAccountExists(email)
            val userId = createUserId(email)
            val user = User(userId = userId, email = email, about = about)
            saveUser(password, user)
            return user
        }

        private fun saveUser(password: String, user: User) {
            users.getOrPut(password, ::mutableListOf).add(user)
        }

        private fun createUserId(email: String): String {
            return email.takeWhile { it != '@' } + "Id"
        }

        private fun checkAccountExists(email: String) {
            if (users.values.flatten().any { it.email == email })
                throw DuplicateAccountException()
        }
    }

    class DuplicateAccountException : Throwable()
}
