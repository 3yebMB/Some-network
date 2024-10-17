package dev.m13d.somenet.domain.post

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val id: String,
    val userId: String,
    val text: String,
    val timestamp: Long,
) : Parcelable
