package ca.josue_lubaki.users.presentation

import ca.josue_lubaki.common.domain.model.User

/**
 * created by Josue Lubaki
 * date : 2023-11-05
 * version : 1.0.0
 */

sealed class ContactState {
    data object Idle : ContactState()
    data object Loading : ContactState()
    data class Success(
        val data: List<User>,
        val me : User? = null
    ) : ContactState()
    data class Error(val error: Exception) : ContactState()
}