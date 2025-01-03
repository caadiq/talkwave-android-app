package com.app.talkwave.model.api

import com.app.talkwave.model.dto.ChatRoomListDto
import com.app.talkwave.model.dto.ChatRoomDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {

    @GET("/api/talkwave/chat/rooms")
    fun getChatRoomList(@Query("userId") userId: String): Call<List<ChatRoomListDto>>

    @GET("/api/talkwave/chat/rooms/{roomId}/messages")
    fun getChatMessageList(@Path("roomId") roomId: Int): Call<ChatRoomDto>
}