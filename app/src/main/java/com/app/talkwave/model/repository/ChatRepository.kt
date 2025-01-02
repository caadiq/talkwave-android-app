package com.app.talkwave.model.repository

import com.app.talkwave.model.api.ChatApi
import com.app.talkwave.model.dto.ChatListDto
import com.app.talkwave.model.dto.ChatRoomDto
import com.app.talkwave.model.utils.RetrofitUtil
import retrofit2.Retrofit
import javax.inject.Inject

class ChatRepository @Inject constructor(retrofit: Retrofit)  {
    private val api: ChatApi = retrofit.create(ChatApi::class.java)

    suspend fun getChatRoomList(): RetrofitUtil.Results<List<ChatListDto>> {
        return RetrofitUtil.call(api.getChatRoomList())
    }

    suspend fun getChatMessageList(roomId: Int): RetrofitUtil.Results<ChatRoomDto> {
        return RetrofitUtil.call(api.getChatMessageList(roomId))
    }
}