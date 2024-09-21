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
import androidx.navigation.navArgument
import dev.m13d.somenet.postcomposer.CreateNewPostScreen
import dev.m13d.somenet.postcomposer.CreatePostViewModel
import dev.m13d.somenet.signup.SignUpScreen
import dev.m13d.somenet.signup.SignUpViewModel
import dev.m13d.somenet.timeline.TimelineScreen
import dev.m13d.somenet.timeline.TimelineViewModel
import dev.m13d.somenet.ui.theme.SoMeNetTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private companion object {
        private const val SIGNUP = "SignUpScreen"
        private const val TIMELINE = "Timeline"
        private const val CREATE_NEW_POST = "CreateNewPost"
    }

    private val signUpViewModel: SignUpViewModel by viewModel()
    private val timelineViewModel: TimelineViewModel by viewModel()
    private val createPostViewModel: CreatePostViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            SoMeNetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = SIGNUP) {
                        composable(SIGNUP) {
                            SignUpScreen(signUpViewModel) { signedUpUserId ->
                                navController.navigate("$TIMELINE/$signedUpUserId") {
                                    popUpTo(SIGNUP) { inclusive = true }
                                }
                            }
                        }
                        composable(
                            route = "$TIMELINE/{userId}",
                            arguments = listOf(navArgument("userId") { })
                        ) { backStackEntry ->
                            TimelineScreen(
                                userId = backStackEntry.arguments?.getString("userId") ?: "",
                                timelineViewModel = timelineViewModel,
                                onCreateNewPost = { navController.navigate(CREATE_NEW_POST) }
                            )
                        }
                        composable(CREATE_NEW_POST) {
                            CreateNewPostScreen(createPostViewModel) {
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
