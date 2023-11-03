package ca.josue_lubaki.login.presentation

/**
 * created by Josue Lubaki
 * date : 2023-11-03
 * version : 1.0.0
 */

sealed class LoginEvent {
    object OnLoadData : LoginEvent()
}