package dev.m13d.somenet.signup

import dev.m13d.somenet.domain.user.User

sealed class SignUpState {

    data class SignedUp(val user: User): SignUpState()

    object BadEmail : SignUpState()

    object BadPassword : SignUpState()

}
