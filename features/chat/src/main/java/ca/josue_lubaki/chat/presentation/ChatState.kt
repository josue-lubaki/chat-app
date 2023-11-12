package ca.josue_lubaki.chat.presentation

import ca.josue_lubaki.common.domain.model.User

/**
 * created by Josue Lubaki
 * date : 2023-11-11
 * version : 1.0.0
 */

sealed class ChatState {
    data object Idle : ChatState()
    data object Loading : ChatState()
    data class Success(val me: User? = null) : ChatState()
    data class Error(val error: Exception) : ChatState()
}