package ca.josue_lubaki.chat.presentation

import ca.josue_lubaki.common.domain.model.PushNotification

/**
 * created by Josue Lubaki
 * date : 2023-11-11
 * version : 1.0.0
 */

sealed class ChatEvent {
    data class OnLoadData(val userId : String) : ChatEvent()
    data class OnSendMessage(
        val receiverId : String,
        val message : String
    ) : ChatEvent()

    data class OnSendNotification(val notification : PushNotification) : ChatEvent()
}