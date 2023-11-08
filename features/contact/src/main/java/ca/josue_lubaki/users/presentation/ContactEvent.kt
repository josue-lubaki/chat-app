package ca.josue_lubaki.users.presentation

/**
 * created by Josue Lubaki
 * date : 2023-11-05
 * version : 1.0.0
 */

sealed class ContactEvent {
    object OnLoadData : ContactEvent()
}