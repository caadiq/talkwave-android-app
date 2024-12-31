package com.app.talkwave.model.dto

data class ChatMessageSendDto(
    val roomId: Int,
    val userId: String,
    val message: String
)
