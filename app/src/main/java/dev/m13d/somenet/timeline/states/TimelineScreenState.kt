package dev.m13d.somenet.timeline.states

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.m13d.somenet.domain.post.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimelineScreenState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    @StringRes val error: Int = 0
) : Parcelable
