package ca.josue_lubaki.chatapp.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ca.josue_lubaki.common.navigation.ScreenTarget
import ca.josue_lubaki.login.presentation.LoginScreen
import ca.josue_lubaki.register.presentation.RegisterScreen

/**
 * created by Josue Lubaki
 * date : 2023-11-03
 * version : 1.0.0
 */

fun NavGraphBuilder.authNavigationGraph(
    navController : NavController,
    windowSize : WindowWidthSizeClass,
) {

    navigation(
        route = Graph.AUTH,
        startDestination = ScreenTarget.Register.route
    ){

        val onNavigateToRoute = { route : String ->
            navController.navigate(route)
        }

        composable(route = ScreenTarget.Login.route){
            LoginScreen(
                windowSize = windowSize,
                onNavigateToRoute = onNavigateToRoute
            )
        }

        composable(route = ScreenTarget.Register.route){
            RegisterScreen(
                windowSize = windowSize,
                onNavigateToRoute = onNavigateToRoute
            )
        }
    }

}