package dev.m13d.somenet.signup.states

import dev.m13d.somenet.domain.user.User

sealed class SignUpState {

    data class SignedUp(val user: User) : SignUpState()

    object BadEmail : SignUpState()

    object BadPassword : SignUpState()

    object DuplicateAccount : SignUpState()

    object BackendError : SignUpState()

    object Offline : SignUpState()

}
