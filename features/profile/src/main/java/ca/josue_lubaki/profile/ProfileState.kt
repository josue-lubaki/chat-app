package ca.josue_lubaki.profile

import ca.josue_lubaki.common.domain.model.User

/**
 * created by Josue Lubaki
 * date : 2023-11-08
 * version : 1.0.0
 */

sealed class ProfileState {
    data object Idle : ProfileState()
    data object Loading : ProfileState()
    data class Success(val data: User?) : ProfileState()
    data class Error(val error: Exception) : ProfileState()
}