package ca.josue_lubaki.register.presentation

import com.google.firebase.auth.FirebaseUser

/**
 * created by Josue Lubaki
 * date : 2023-10-26
 * version : 1.0.0
 */

sealed class RegisterState {
    data object Idle : RegisterState()
    data object Loading : RegisterState()
    data object Success : RegisterState()
    data class Error(val error: java.lang.Exception?) : RegisterState()
}