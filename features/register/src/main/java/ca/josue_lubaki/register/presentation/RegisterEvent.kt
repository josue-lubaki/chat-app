package ca.josue_lubaki.register.presentation

/**
 * created by Josue Lubaki
 * date : 2023-10-26
 * version : 1.0.0
 */

sealed class RegisterEvent {
    data class OnRegister(
        val username : String,
        val email : String,
        val password : String,
    ) : RegisterEvent()
}