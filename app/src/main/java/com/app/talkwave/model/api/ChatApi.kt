package com.app.talkwave.model.api

import com.app.talkwave.model.dto.ChatListDto
import com.app.talkwave.model.dto.ChatRoomDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatApi {

    @GET("/api/talkwave/chat/rooms")
    fun getChatRoomList(): Call<List<ChatListDto>>

    @GET("/api/talkwave/chat/rooms/{roomId}/messages")
    fun getChatMessageList(@Path("roomId") roomId: Int): Call<ChatRoomDto>
}