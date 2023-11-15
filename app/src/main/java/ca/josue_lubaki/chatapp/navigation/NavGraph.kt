package ca.josue_lubaki.chatapp.navigation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ca.josue_lubaki.chat.presentation.ChatScreen
import ca.josue_lubaki.chatapp.MainActivity
import ca.josue_lubaki.common.navigation.ScreenTarget
import ca.josue_lubaki.common.utils.Constants.ARGS_USER_ID_KEY
import ca.josue_lubaki.profile.ProfileScreen
import ca.josue_lubaki.users.presentation.ContactsScreen

/**
 * created by Josue Lubaki
 * date : 2023-11-03
 * version : 1.0.0
 */

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NavGraph(
    navController : NavHostController,
    windowSize : WindowWidthSizeClass,
    senderId : String? = null
){

    LaunchedEffect(key1 = senderId){
        if(!senderId.isNullOrEmpty()){
            navController.navigate(ScreenTarget.Chat.routeWithArgs(senderId))
        }
    }

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTH
    ){

        val onNavigateToRoute = { route : String ->
            navController.navigate(route)
        }

        val onBackPressed = {
            navController.popBackStack()
        }

        val onNavigateToProfile = {
            navController.navigate(ScreenTarget.Profile.route)
        }

        val onNavigateToChat = { userId : String ->
            navController.navigate(ScreenTarget.Chat.routeWithArgs(userId))
        }

        authNavigationGraph(
            navController = navController,
            windowSize = windowSize
        )

        composable(ScreenTarget.Contact.route){
            ContactsScreen(
                onNavigateToProfile = onNavigateToProfile,
                onNavigateToChat = onNavigateToChat
            )
        }

        composable(ScreenTarget.Profile.route){
            ProfileScreen(
                onNavigateToRoute = { route : String ->
                    navController.navigate(route)
                },
                onBackPressed = onBackPressed
            )
        }

        composable(
            route = ScreenTarget.Chat.route,
            arguments = listOf(navArgument(ARGS_USER_ID_KEY){
                type = NavType.StringType
            })
        ){
            val userId = it.arguments?.getString(ARGS_USER_ID_KEY) ?: ""

            ChatScreen(
                userId = userId,
                windowSize = windowSize,
                onNavigateToProfile = onNavigateToProfile,
                onBackPressed = {
                    if (senderId.isNullOrEmpty()) onBackPressed()
                    else {
                        onNavigateToRoute(ScreenTarget.Contact.route)
                        true
                    }
                }
            )
        }
    }
}