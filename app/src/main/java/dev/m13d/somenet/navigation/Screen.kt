package dev.m13d.somenet.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.m13d.somenet.R

sealed class Screen(val route: String) {

    object SignUp : Screen("SignUpScreen")

    object Home : Screen("home/{userId}") {
        const val userId = "userId"
        fun createRoute(userId: String) = "home/$userId"
    }

    sealed class MainScreen(
        route: String,
        @StringRes val title: Int,
        @DrawableRes val icon: Int,
    ) : Screen(route) {

        object Timeline : MainScreen("Timeline", R.string.timeline, R.drawable.ic_timeline)

        object Friends : MainScreen("Friends", R.string.friends, R.drawable.ic_friends)
    }

    object PostComposer : Screen("CreateNewPost")
}
