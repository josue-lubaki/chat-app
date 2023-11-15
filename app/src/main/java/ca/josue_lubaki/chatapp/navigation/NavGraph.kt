package ca.josue_lubaki.chatapp.navigation


import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ca.josue_lubaki.chat.presentation.ChatScreen
import ca.josue_lubaki.common.navigation.ScreenTarget
import ca.josue_lubaki.common.utils.Constants.ARGS_USER_ID_KEY
import ca.josue_lubaki.profile.ProfileScreen
import ca.josue_lubaki.users.presentation.ContactsScreen

/**
 * created by Josue Lubaki
 * date : 2023-11-03
 * version : 1.0.0
 */

@Composable
fun NavGraph(
    navController : NavHostController,
    windowSize : WindowWidthSizeClass,
    route : String? = null
){
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = if(route.isNullOrEmpty()) Graph.AUTH else route
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
                onBackPressed = onBackPressed
            )
        }

    }
}