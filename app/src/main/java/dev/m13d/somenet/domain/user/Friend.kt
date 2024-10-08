package dev.m13d.somenet.domain.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Friend(
    val user: User,
    val isFollowee: Boolean,
) : Parcelable
