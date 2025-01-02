package com.app.talkwave.viewmodel

import androidx.lifecycle.MutableLiveData
import com.app.talkwave.model.dto.ChatListDto
import com.app.talkwave.model.dto.ChatMessageReceiveDto
import com.app.talkwave.model.dto.MemberListDto
import com.app.talkwave.model.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val repository: ChatRepository) : BaseViewModel() {
    private val _roomList = MutableLiveData<List<ChatListDto>>()
    val roomList: MutableLiveData<List<ChatListDto>> = _roomList

    private val _chatRoomName = MutableLiveData<String>()
    val chatRoomName: MutableLiveData<String> = _chatRoomName

    private val _memberList = MutableLiveData<List<MemberListDto>>()
    val memberList: MutableLiveData<List<MemberListDto>> = _memberList

    private val _messageList = MutableLiveData<MutableList<ChatMessageReceiveDto>>()
    val messageList: MutableLiveData<MutableList<ChatMessageReceiveDto>> = _messageList

    fun getRoomList() {
        execute(
            call = { repository.getChatRoomList() },
            onSuccess = { data -> _roomList.postValue(data) }
        )
    }

    fun getChatRoom(roomId: Int) {
        execute(
            call = { repository.getChatMessageList(roomId) },
            onSuccess = { data ->
                _chatRoomName.postValue(data.roomName)
                _messageList.postValue(data.chatList.toMutableList())
                _memberList.postValue(data.userList)
            }
        )
    }

    fun addMessage(dto : ChatMessageReceiveDto) {
        _messageList.value?.add(dto)
        _messageList.postValue(_messageList.value)
    }
}