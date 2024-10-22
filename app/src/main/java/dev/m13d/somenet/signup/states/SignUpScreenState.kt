package dev.m13d.somenet.signup.states

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpScreenState(
    val isLoading: Boolean = false,
    val email: String = "",
    val isBadEmail:Boolean = false,
    val password: String = "",
    val isBadPassword: Boolean = false,
    val about: String = "",
    val signedUpUserId: String = "",
    @StringRes val error: Int = 0,
) : Parcelable
