package dev.m13d.somenet.domain.friends

import dev.m13d.somenet.domain.user.Friend

interface FriendsCatalog {

    fun loadFriendsFor(userId: String): List<Friend>
}
