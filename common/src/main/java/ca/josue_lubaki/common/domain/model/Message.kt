package ca.josue_lubaki.common.domain.model

data class Message(
    val messageId : String = "",
    val senderId : String = "",
    val receiverId : String = "",
    val message : String = "",
)
