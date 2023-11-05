package ca.josue_lubaki.dashboard.presentation

/**
 * created by Josue Lubaki
 * date : 2023-11-05
 * version : 1.0.0
 */

sealed class DashboardState {
    object Idle : DashboardState()
    object Loading : DashboardState()
//    data class Success(val data: Dashboard) : DashboardState()
    data class Error(val error: Exception) : DashboardState()
}