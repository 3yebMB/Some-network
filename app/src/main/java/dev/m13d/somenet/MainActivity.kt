package dev.m13d.somenet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.m13d.somenet.postcomposer.CreateNewPostScreen
import dev.m13d.somenet.signup.SignUpScreen
import dev.m13d.somenet.timeline.TimelineScreen
import dev.m13d.somenet.ui.theme.SoMeNetTheme

class MainActivity : ComponentActivity() {

    sealed class Screen(val route: String) {

        object SignUp : Screen("SignUpScreen")

        object Home : Screen("home/{userId}") {
            fun createRoute(userId: String) = "home/$userId"
        }

        sealed class MainScreen(
            route: String,
            @StringRes val title: Int,
            @DrawableRes val icon: Int,
        ) : Screen(route) {

            object Timeline : MainScreen("Timeline", R.string.timeline, R.drawable.ic_timeline)

            object Friends : MainScreen("Timeline", R.string.friends, R.drawable.ic_friends)
        }

        object PostComposer : Screen("CreateNewPost")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SoMeNetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.SignUp.route) {
                        composable(Screen.SignUp.route) {
                            SignUpScreen { signedUpUserId ->
                                navController.navigate(Screen.Home.createRoute(signedUpUserId)) {
                                    popUpTo(Screen.SignUp.route) { inclusive = true }
                                }
                            }
                        }
                        composable(route = Screen.Home.route) { backStackEntry ->
                            HomeScreen(userId = backStackEntry.arguments?.getString("userId") ?: "")
                        }
                    }
//                        modifier = Modifier.padding(innerPadding)
                }
            }
        }
    }

    @Composable
    fun HomeScreen(userId: String) {
        val navController = rememberNavController()
        val homeScreens = listOf(Screen.MainScreen.Timeline, Screen.MainScreen.Friends)
        val currentDestination = currentDestination(navController)
        val isMainScreenDestination = homeScreens.any { it.route == currentDestination }
        Scaffold(bottomBar = {
            if (isMainScreenDestination) {
                HomeScreenBottomNavigation(navController, homeScreens)
            }
        }) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = homeScreens.first().route,
                modifier = Modifier.padding(if (isMainScreenDestination) 50.dp else 0.dp),
            ) {
                composable(route = Screen.MainScreen.Timeline.route) {
                    TimelineScreen(
                        userId = userId,
                    ) { navController.navigate(Screen.PostComposer.route) }
                }
                composable(route = Screen.PostComposer.route) {
                    CreateNewPostScreen {
                        navController.navigateUp()
                    }
                }
                composable(route = "friends") {
                    Friends()
                }
            }
        }
    }

    @Composable
    private fun HomeScreenBottomNavigation(
        navController: NavHostController,
        homeScreens: List<Screen.MainScreen>,
    ) {
        val currentDestination = currentDestination(navController)
        NavigationBar {
            homeScreens.forEach { screen ->
                val title = stringResource(id = screen.title)
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = screen.icon),
                            contentDescription = title,
                        )
                    },
                    label = { Text(text = title) },
                    selected = currentDestination == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

    @Composable
    private fun currentDestination(navController: NavHostController): String? {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        return navBackStackEntry?.destination?.route
    }

    @Composable
    fun Friends() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = stringResource(R.string.friends))
        }
    }
}
