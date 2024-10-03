package dev.m13d.somenet.friends.states

import dev.m13d.somenet.domain.user.Friend

sealed class FriendsState {

    data class Loaded(val friends: List<Friend>) : FriendsState()

}