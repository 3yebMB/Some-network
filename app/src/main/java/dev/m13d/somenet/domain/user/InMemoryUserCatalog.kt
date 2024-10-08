package dev.m13d.somenet.domain.user

import dev.m13d.somenet.domain.exceptions.DuplicateAccountException
import dev.m13d.somenet.domain.friends.ToggleFollowing

typealias PasswordUserType = MutableMap<String, MutableList<User>>

class InMemoryUserCatalog(
    private val users: PasswordUserType = mutableMapOf(),
    private val followings: MutableList<Following> = mutableListOf(),
) : UserCatalog {

    private val allUsers: List<User>
        get() = users.values.flatten()

    override suspend fun createUser(
        email: String,
        password: String,
        about: String,
    ): User {
        checkAccountExists(email)
        val userId = createUserId(email)
        val user = User(id = userId, email = email, about = about)
        saveUser(password, user)
        return user
    }

    override suspend fun followedBy(userId: String): List<String> {
        return followings
            .filter { it.userId == userId }
            .map { it.followedId }
    }

    override suspend fun loadFriendsFor(userId: String): List<Friend> {
        val friendsFollowedByUser = followedBy(userId)
        return allUsers
            .filter { it.id != userId }
            .map { user -> Friend(user, user.id in friendsFollowedByUser) }
    }

    override fun toggleFollowing(userId: String, followeeId: String): ToggleFollowing {
        val following = Following(userId, followeeId)
        return if (followings.contains(following)) {
            followings.remove(following)
            ToggleFollowing(following, false)
        } else {
            followings.add(following)
            ToggleFollowing(following, true)
        }
    }

    private fun checkAccountExists(email: String) {
        if (allUsers.any { it.email == email })
            throw DuplicateAccountException()
    }

    private fun createUserId(email: String): String {
        return email.takeWhile { it != '@' } + "Id"
    }

    private fun saveUser(password: String, user: User) {
        users.getOrPut(password, ::mutableListOf).add(user)
    }
}
