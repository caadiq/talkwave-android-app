package com.app.talkwave.model.dto

data class ChatRoomListDto(
    val roomId: Int,
    val roomName: String,
    val latestMessage: String,
    val sendDate: String,
)