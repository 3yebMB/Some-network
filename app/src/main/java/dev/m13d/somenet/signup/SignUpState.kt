package dev.m13d.somenet.signup

sealed class SignUpState {

    object BadEmail : SignUpState()

    object BadPassword : SignUpState()

}
