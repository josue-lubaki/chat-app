package ca.josue_lubaki.chat.domain

data class Message(
    val senderId : String,
    val receiverId : String,
    val message : String,
)
