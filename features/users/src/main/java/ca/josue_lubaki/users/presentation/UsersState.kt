package ca.josue_lubaki.users.presentation

import ca.josue_lubaki.users.domain.model.User

/**
 * created by Josue Lubaki
 * date : 2023-11-05
 * version : 1.0.0
 */

sealed class UsersState {
    data object Idle : UsersState()
    data object Loading : UsersState()
    data class Success(val data: List<User>) : UsersState()
    data class Error(val error: Exception) : UsersState()
}