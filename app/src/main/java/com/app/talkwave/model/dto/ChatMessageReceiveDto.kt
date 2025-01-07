package com.app.talkwave.model.dto

data class ChatMessageReceiveDto(
    val userId: String,
    val userName: String,
    val message: String,
    val sendDate: String,
    val emojiUrl: String?,
    var isFirstMessage: Boolean = false
)