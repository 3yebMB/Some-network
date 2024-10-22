package dev.m13d.somenet.signup

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dev.m13d.somenet.MainActivity
import dev.m13d.somenet.domain.user.ControllableUserCatalog
import dev.m13d.somenet.domain.user.InMemoryUserCatalog
import dev.m13d.somenet.domain.user.OfflineUserCatalog
import dev.m13d.somenet.domain.user.UnavailableUserCatalog
import dev.m13d.somenet.domain.user.User
import dev.m13d.somenet.domain.user.UserCatalog
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class SignUpScreenTest {

    @get:Rule
    val testRule = createAndroidComposeRule<MainActivity>()

    private val signUpModule = module {
        factory<UserCatalog> { InMemoryUserCatalog() }
    }

    @Before
    fun setUp() {
        loadKoinModules(signUpModule)
    }

    @Test
    fun performSignUp() {
        launchSignUpScreen(testRule) {
            typeEmail("somemail@somenet.dev")
            typePassword("p@sSw0rd")
            submit()
        } verify {
            timelineScreenIsPresent()
        }
    }

    @Test
    fun displayBadEmailError() {
        launchSignUpScreen(testRule) {
            typeEmail("email")
            submit()
        } verify {
            badEmailErrorIsShown()
        }
    }

    @Test
    fun resetBadEmailError() {
        launchSignUpScreen(testRule) {
            typeEmail("email")
            submit()
            typeEmail("email@")
        } verify {
            badEmailErrorIsNotShown()
        }
    }

    @Test
    fun displayBadPasswordError() {
        launchSignUpScreen(testRule) {
            typeEmail("jov@somenet.dev")
            typePassword("abc")
            submit()
        } verify {
            badPasswordErrorIsShown()
        }
    }

    @Test
    fun resetBadPasswordError() {
        launchSignUpScreen(testRule) {
            typeEmail("valid@somenet.dev")
            typePassword("pass")
            submit()
            typePassword("pass@")
        } verify {
            badPasswordErrorIsNotShown()
        }
    }

    @Test
    fun displayDuplicateAccountError() = runBlocking<Unit> {
        val signedUpUserEmail = "alice@somenet.dev"
        val signedUpUserPassword = "@l1cePass"

        replaceUserCatalogWith(InMemoryUserCatalog().apply {
            createUser(signedUpUserEmail, signedUpUserPassword, "")
        })

        launchSignUpScreen(testRule) {
            typeEmail(signedUpUserEmail)
            typePassword(signedUpUserPassword)
            submit()
        } verify {
            duplicateAccountErrorIsShown()
        }
    }

    @Test
    fun displayBackendError() {
        replaceUserCatalogWith(UnavailableUserCatalog())

        launchSignUpScreen(testRule) {
            typeEmail("robert@somenet.dev")
            typePassword("J0hn#333")
            submit()
        } verify {
            backendErrorIsShown()
        }
    }

    @Test
    fun displayOfflineError() {
        replaceUserCatalogWith(OfflineUserCatalog())

        launchSignUpScreen(testRule) {
            typeEmail("philip@somenet.dev")
            typePassword("J0hn#333")
            submit()
        } verify {
            offlineErrorIsShown()
        }
    }

    @Test
    fun displayLoadingBlock() {
        val createUser: suspend (String, String, String) -> User = { id, email, about ->
            delay(3500L)
            User(id, email, about)
        }
        replaceUserCatalogWith(ControllableUserCatalog(userCreate = createUser))

        launchSignUpScreen(testRule) {
            typeEmail("alex@somenet.dev")
            typePassword("@1eX1092")
            submit()
        } verify {
            loadingBlockIsShown()
        }
    }

    @After
    fun tearDown() {
        replaceUserCatalogWith(InMemoryUserCatalog())
    }

    private fun replaceUserCatalogWith(userCatalog: UserCatalog) {
        val replaceModule = module {
            factory { userCatalog }
        }
        loadKoinModules(replaceModule)
    }
}
