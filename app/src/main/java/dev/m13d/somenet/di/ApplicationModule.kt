package dev.m13d.somenet.di

import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.domain.user.UserCatalog
import dev.m13d.somenet.domain.user.UserRepository
import dev.m13d.somenet.domain.validation.RegexCredentialValidator
import dev.m13d.somenet.signup.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {

    single<UserCatalog> { InMemoryUserCatalog() }
    factory { RegexCredentialValidator() }
    factory { UserRepository(userCatalog = get()) }

    viewModel {
        SignUpViewModel(
            credentialValidator = get(),
            userRepository = get(),
        )
    }
}
