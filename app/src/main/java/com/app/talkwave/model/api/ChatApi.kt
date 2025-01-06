package com.app.talkwave.model.api

import com.app.talkwave.model.dto.ChatRoomAddDto
import com.app.talkwave.model.dto.ChatRoomListDto
import com.app.talkwave.model.dto.ChatRoomDto
import com.app.talkwave.model.dto.ChatRoomLeaveDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {

    @GET("/api/talkwave/chat/rooms")
    fun getChatRoomList(@Query("userId") userId: String): Call<List<ChatRoomListDto>>

    @POST("/api/talkwave/chat/rooms")
    fun addChatRoom(@Body dto: ChatRoomAddDto): Call<Unit>

    @GET("/api/talkwave/chat/rooms/{roomId}/messages")
    fun getChatMessageList(@Path("roomId") roomId: Int): Call<ChatRoomDto>

    @POST("/api/talkwave/chat/rooms/leave")
    fun leaveChatRoom(@Body dto: ChatRoomLeaveDto): Call<Unit>
}