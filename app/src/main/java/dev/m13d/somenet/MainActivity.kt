package dev.m13d.somenet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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

    private companion object {
        private const val SIGNUP = "SignUpScreen"
        private const val TIMELINE = "Timeline"
        private const val CREATE_NEW_POST = "CreateNewPost"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SoMeNetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = SIGNUP) {
                        composable(SIGNUP) {
                            SignUpScreen { signedUpUserId ->
                                navController.navigate("home/$signedUpUserId") {
                                    popUpTo(SIGNUP) { inclusive = true }
                                }
                            }
                        }
                        composable(route = "home/{userId}") { backStackEntry ->
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
        Scaffold(bottomBar = {
            HomeScreenBottomNavigation(navController)
        }) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = TIMELINE,
                modifier = Modifier.padding(50.dp),
            ) {
                composable(route = TIMELINE) {
                    TimelineScreen(
                        userId = userId,
                    ) { navController.navigate(CREATE_NEW_POST) }
                }
                composable(CREATE_NEW_POST) {
                    CreateNewPostScreen {
                        navController.navigateUp()
                    }
                }
                composable("friends") {
                    Friends()
                }
            }
        }
    }

    @Composable
    private fun HomeScreenBottomNavigation(navController: NavHostController) {
        val screens = listOf(TIMELINE, "friends")
        val currentDestination = currentDestination(navController)
        NavigationBar {
            screens.forEach { screen ->
                NavigationBarItem(
                    icon = { Icons.Default.Add },
                    label = { Text(text = screen) },
                    selected = currentDestination == screen,
                    onClick = {
                        navController.navigate(screen) {
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
