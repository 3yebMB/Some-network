package dev.m13d.somenet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
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
import dev.m13d.somenet.home.HomeScreen
import dev.m13d.somenet.navigation.Screen
import dev.m13d.somenet.postcomposer.CreateNewPostScreen
import dev.m13d.somenet.signup.SignUpScreen
import dev.m13d.somenet.timeline.TimelineScreen
import dev.m13d.somenet.ui.theme.SoMeNetTheme

class MainActivity : ComponentActivity() {

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
}
