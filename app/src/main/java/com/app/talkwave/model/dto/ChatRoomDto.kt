package com.app.talkwave.model.dto

data class ChatRoomDto(
    val roomId: Int,
    val roomName: String,
    val userList: List<MemberListDto>,
    val chatList: List<ChatMessageReceiveDto>
)