package dev.m13d.somenet.app

import dev.m13d.somenet.domain.friends.FriendsRepository
import dev.m13d.somenet.domain.post.InMemoryPostsCatalog
import dev.m13d.somenet.domain.post.PostRepository
import dev.m13d.somenet.domain.post.PostsCatalog
import dev.m13d.somenet.domain.timeline.TimelineRepository
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.domain.user.InMemoryUserDataStore
import dev.m13d.somenet.domain.user.UserCatalog
import dev.m13d.somenet.domain.user.UserDataStore
import dev.m13d.somenet.domain.user.UserRepository
import dev.m13d.somenet.domain.validation.RegexCredentialValidator
import dev.m13d.somenet.friends.FriendsViewModel
import dev.m13d.somenet.postcomposer.CreatePostViewModel
import dev.m13d.somenet.signup.SignUpViewModel
import dev.m13d.somenet.timeline.TimelineViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {

    single<CoroutineDispatchers> { DefaultDispatchers() }
    single<UserCatalog> { InMemoryUserCatalog() }
    single<PostsCatalog> { InMemoryPostsCatalog() }
    single<UserDataStore> { InMemoryUserDataStore() }
    factory { RegexCredentialValidator() }
    factory { UserRepository(userCatalog = get(), userDataStore = get()) }
    factory { TimelineRepository(userCatalog = get(), postCatalog = get()) }
    factory { PostRepository(userData = get(), postsCatalog = get()) }
    factory { FriendsRepository(userCatalog = get()) }

    viewModel {
        SignUpViewModel(
            credentialValidator = get(),
            userRepository = get(),
            dispatchers = get(),
        )
    }

    viewModel {
        TimelineViewModel(
            timelineRepository = get(),
            dispatchers = get(),
        )
    }

    viewModel {
        CreatePostViewModel(
            postRepository = get(),
            dispatchers = get(),
        )
    }

    viewModel {
        FriendsViewModel(
            friendsRepository = get(),
            dispatchers = get(),
            savedStateHandle = get(),
        )
    }
}
