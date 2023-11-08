package ca.josue_lubaki.profile

/**
 * created by Josue Lubaki
 * date : 2023-11-08
 * version : 1.0.0
 */

sealed class ProfileEvent {
    object OnLoadData : ProfileEvent()
}