package com.app.talkwave.model.dto

class ChatMessageReceiveDto(
    val chatId: Int,
    val userId: String,
    val userName: String,
    val message: String,
    val sendDate: String
)