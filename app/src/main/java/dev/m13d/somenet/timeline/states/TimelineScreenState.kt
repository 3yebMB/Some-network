package dev.m13d.somenet.timeline.states

import android.os.Parcelable
import androidx.annotation.StringRes
import dev.m13d.somenet.domain.post.Post
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimelineScreenState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    @StringRes val error: Int = 0
) : Parcelable
