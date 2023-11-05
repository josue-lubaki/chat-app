package ca.josue_lubaki.chatapp.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ca.josue_lubaki.common.navigation.ScreenTarget
import ca.josue_lubaki.dashboard.presentation.DashboardScreen

/**
 * created by Josue Lubaki
 * date : 2023-11-03
 * version : 1.0.0
 */

@Composable
fun NavGraph(
    navController : NavHostController,
    windowSize : WindowWidthSizeClass,
){

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTH
    ){
//        composable(ScreenTarget.Splash.route){
//            SplashScreen(navController = navController)
//        }

        authNavigationGraph(
            navController = navController,
            windowSize = windowSize
        )

        composable(ScreenTarget.Dashboard.route){
            DashboardScreen(
                onNavigateToRoute = { route : String ->
                    navController.navigate(route)
                }
            )
        }

    }
}