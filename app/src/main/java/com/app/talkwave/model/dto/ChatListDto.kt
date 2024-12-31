package com.app.talkwave.model.dto

data class ChatListDto(
    val roomId: Int,
    val roomName: String,
    val latestMessage: String,
    val sendDate: String,
)