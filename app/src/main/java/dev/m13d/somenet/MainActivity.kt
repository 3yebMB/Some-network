package dev.m13d.somenet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
                                navController.navigate("$TIMELINE/$signedUpUserId") {
                                    popUpTo(SIGNUP) { inclusive = true }
                                }
                            }
                        }
                        composable(route = "$TIMELINE/{userId}") { backStackEntry ->
                            TimelineScreen(
                                userId = backStackEntry.arguments?.getString("userId") ?: "",
                                onCreateNewPost = { navController.navigate(CREATE_NEW_POST) }
                            )
                        }
                        composable(CREATE_NEW_POST) {
                            CreateNewPostScreen {
                                navController.navigateUp()
                            }
                        }
                    }
//                        modifier = Modifier.padding(innerPadding)
                }
            }
        }
    }
}
