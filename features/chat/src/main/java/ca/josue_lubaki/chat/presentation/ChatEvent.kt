package ca.josue_lubaki.chat.presentation

import ca.josue_lubaki.chat.domain.Message

/**
 * created by Josue Lubaki
 * date : 2023-11-11
 * version : 1.0.0
 */

sealed class ChatEvent {
    data object OnLoadData : ChatEvent()
    data class OnSendMessage(
        val receiverId : String,
        val message : String
    ) : ChatEvent()
}