package dev.m13d.somenet.domain.validation

sealed class CredentialsValidationResult {

    object InvalidEmail : CredentialsValidationResult()

    object InvalidPassword : CredentialsValidationResult()
}
