package ca.josue_lubaki.common.navigation

/**
 * created by Josue Lubaki
 * date : 2023-11-03
 * version : 1.0.0
 */

sealed class ScreenTarget(val route : String) {
    data object Login : ScreenTarget("login")
    data object Register : ScreenTarget("register")
    data object Users : ScreenTarget("users")
    data object Splash : ScreenTarget("splash")
}