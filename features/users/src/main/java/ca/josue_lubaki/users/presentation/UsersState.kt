package ca.josue_lubaki.users.presentation

/**
 * created by Josue Lubaki
 * date : 2023-11-05
 * version : 1.0.0
 */

sealed class UsersState {
    object Idle : UsersState()
    object Loading : UsersState()
//    data class Success(val data: Dashboard) : DashboardState()
    data class Error(val error: Exception) : UsersState()
}