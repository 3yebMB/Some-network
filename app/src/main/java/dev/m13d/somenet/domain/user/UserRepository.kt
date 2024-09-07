package dev.m13d.somenet.domain.user

import dev.m13d.somenet.domain.exceptions.DuplicateAccountException
import dev.m13d.somenet.signup.SignUpState

class UserRepository(
    private val userCatalog: InMemoryUserCatalog,
) {
    fun signUp(
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
}
