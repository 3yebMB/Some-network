package dev.m13d.somenet.signup

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dev.m13d.somenet.MainActivity
import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.domain.user.UserCatalog
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class SignUpTest {

    @get:Rule
    val signUpTestRule = createAndroidComposeRule<MainActivity>()

    private val userCatalog = InMemoryUserCatalog()
    private val signUpModule = module {
        factory<UserCatalog> { userCatalog }
    }

    @Before
    fun setUp() {
        loadKoinModules(signUpModule)
    }

    @Test
    fun performSignUp() {
        launchSignUpScreen(signUpTestRule) {
            typeEmail("somemail@somenet.dev")
            typePassword("p@\$Sw0rd")
            submit()
        } verify {
            timelineScreenIsPresent()
        }
    }

    @Test
    fun displayDuplicateAccountError() {
        val signedUpUserEmail = "alice@somenet.dev"
        val signedUpUserPassword = "@l1cePass"

        userCatalog.createUser(signedUpUserEmail, signedUpUserPassword, "")

        launchSignUpScreen(signUpTestRule) {
            typeEmail(signedUpUserEmail)
            typePassword(signedUpUserPassword)
            submit()
        } verify {
            duplicateAccountErrorIsShown()
        }
    }

    @Test
    fun displayBackendError() {
        val replaceModule = module {
            factory<UserCatalog> { UnavailableUserCatalog() }
        }
        loadKoinModules(replaceModule)

        launchSignUpScreen(signUpTestRule) {
            typeEmail("robert@somenet.dev")
            typePassword("J0hn#333")
            submit()
        } verify {
            backendErrorIsShown()
        }
    }

    @Test
    fun displayOfflineError() {
        val replaceModule = module {
            factory<UserCatalog> { OfflineUserCatalog() }
        }
        loadKoinModules(replaceModule)

        launchSignUpScreen(signUpTestRule) {
            typeEmail("philip@somenet.dev")
            typePassword("J0hn#333")
            submit()
        } verify {
            offlineErrorIsShown()
        }
    }

    @After
    fun tearDown() {
        val resetModule = module {
            single<UserCatalog> { InMemoryUserCatalog() }
        }
        loadKoinModules(resetModule)
    }
}

class OfflineUserCatalog : UserCatalog {
    override fun createUser(email: String, password: String, about: String): User {
        throw ConnectionUnavailableException()
    }
}

class UnavailableUserCatalog : UserCatalog {
    override fun createUser(email: String, password: String, about: String): User {
        throw BackendException()
    }
}
