package dev.m13d.somenet.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import dev.m13d.somenet.friends.FriendsScreen
import dev.m13d.somenet.navigation.Screen
import dev.m13d.somenet.postcomposer.CreateNewPostScreen
import dev.m13d.somenet.timeline.TimelineScreen

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
                TimelineScreen(userId = userId) {
                    navController.navigate(Screen.PostComposer.route)
                }
            }
            composable(route = Screen.PostComposer.route) {
                CreateNewPostScreen {
                    navController.navigateUp()
                }
            }
            composable(route = Screen.MainScreen.Friends.route) {
                FriendsScreen(userId = userId)
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
