package com.app.talkwave.model.repository

import com.app.talkwave.model.api.ChatApi
import com.app.talkwave.model.dto.ChatRoomAddDto
import com.app.talkwave.model.dto.ChatRoomListDto
import com.app.talkwave.model.dto.ChatRoomDto
import com.app.talkwave.model.dto.ChatRoomLeaveDto
import com.app.talkwave.model.utils.RetrofitUtil
import retrofit2.Retrofit
import javax.inject.Inject

class ChatRepository @Inject constructor(retrofit: Retrofit)  {
    private val api: ChatApi = retrofit.create(ChatApi::class.java)

    suspend fun getChatRoomList(userId: String): RetrofitUtil.Results<List<ChatRoomListDto>> {
        return RetrofitUtil.call(api.getChatRoomList(userId))
    }

    suspend fun addChatRoom(dto: ChatRoomAddDto): RetrofitUtil.Results<Unit> {
        return RetrofitUtil.call(api.addChatRoom(dto))
    }

    suspend fun getChatMessageList(roomId: Int): RetrofitUtil.Results<ChatRoomDto> {
        return RetrofitUtil.call(api.getChatMessageList(roomId))
    }

    suspend fun leaveChatRoom(dto: ChatRoomLeaveDto): RetrofitUtil.Results<Unit> {
        return RetrofitUtil.call(api.leaveChatRoom(dto))
    }
}