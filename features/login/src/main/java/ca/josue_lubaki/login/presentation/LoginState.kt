package ca.josue_lubaki.login.presentation

import androidx.annotation.StringRes

/**
 * created by Josue Lubaki
 * date : 2023-11-03
 * version : 1.0.0
 */

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data class Error(val error: Exception) : LoginState()
}