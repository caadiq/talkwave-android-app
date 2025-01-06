package com.app.talkwave.model.dto

data class ChatRoomAddDto(
    val roomName: String,
    val userId: String,
    val userList: List<String>
)
