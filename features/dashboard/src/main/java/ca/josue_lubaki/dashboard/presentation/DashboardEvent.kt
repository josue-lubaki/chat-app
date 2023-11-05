package ca.josue_lubaki.dashboard.presentation

/**
 * created by Josue Lubaki
 * date : 2023-11-05
 * version : 1.0.0
 */

sealed class DashboardEvent {
    object OnLoadData : DashboardEvent()
}