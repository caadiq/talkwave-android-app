package com.app.talkwave.model.dto

class ChatMessageDto(
    val chatId: Int,
    val userId: String,
    val userName: String,
    val message: String,
    val date: String
)