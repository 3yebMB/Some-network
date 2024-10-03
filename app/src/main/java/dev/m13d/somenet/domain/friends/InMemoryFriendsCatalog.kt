package dev.m13d.somenet.domain.friends

import dev.m13d.somenet.domain.exceptions.BackendException
import dev.m13d.somenet.domain.exceptions.ConnectionUnavailableException
import dev.m13d.somenet.domain.user.Friend
import dev.m13d.somenet.domain.user.User

class InMemoryFriendsCatalog {

    private val tom = Friend(User("tomId", ":email:", ":about:"), isFollow = false)
    private val anna = Friend(User("annaId", "", ""), isFollow = true)
    private val sara = Friend(User("saraId", "", ""), isFollow = false)
    private val friendsForUserId = mapOf(
        "jerryId" to listOf(tom),
        "lucyId" to listOf(anna, sara, tom),
        "samId" to emptyList(),
    )

    fun loadFriendsFor(userId: String): List<Friend> {
        when(userId) {
            "mihalyId" -> throw BackendException()
            "jovId" -> throw ConnectionUnavailableException()
        }
        return friendsForUserId.getValue(userId)
    }
}
