package dev.m13d.somenet.friends.states

import android.os.Parcelable
import androidx.annotation.StringRes
import dev.m13d.somenet.domain.user.Friend
import kotlinx.parcelize.Parcelize

@Parcelize
data class FriendsScreenState(
    val isLoading: Boolean = false,
    val friends: List<Friend> = emptyList(),
    val currentlyUpdatingFriends: List<String> = emptyList(),
    @StringRes val error: Int = 0,
) : Parcelable
