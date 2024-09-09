package dev.m13d.somenet.domain.user

import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.domain.exceptions.DuplicateAccountException
import dev.m13d.somenet.signup.states.SignUpState

class UserRepository(
    private val userCatalog: UserCatalog,
) {
    suspend fun signUp(
        email: String,
        password: String,
        about: String
    ): SignUpState {
        return try {
            val user = userCatalog.createUser(email, password, about)
            SignUpState.SignedUp(user)
        } catch (duplicateAccount: DuplicateAccountException) {
            SignUpState.DuplicateAccount
        } catch (backendException: BackendException) {
            SignUpState.BackendError
        } catch (offlineUnavailableException: ConnectionUnavailableException) {
            SignUpState.Offline
        }
    }
}
