package com.app.talkwave.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.talkwave.model.dto.ChatListDto
import com.app.talkwave.model.dto.ChatMessageReceiveDto
import com.app.talkwave.model.dto.MemberListDto
import com.app.talkwave.model.repository.ChatRepository
import com.app.talkwave.model.utils.RetrofitUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val repository: ChatRepository) : ViewModel() {
    private val _roomList = MutableLiveData<List<ChatListDto>>()
    val roomList: MutableLiveData<List<ChatListDto>> = _roomList

    private val _chatRoomName = MutableLiveData<String>()
    val chatRoomName: MutableLiveData<String> = _chatRoomName

    private val _memberList = MutableLiveData<List<MemberListDto>>()
    val memberList: MutableLiveData<List<MemberListDto>> = _memberList

    private val _messageList = MutableLiveData<MutableList<ChatMessageReceiveDto>>()
    val messageList: MutableLiveData<MutableList<ChatMessageReceiveDto>> = _messageList

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun getRoomList() {
        viewModelScope.launch {
            when (val result = repository.getChatRooms()) {
                is RetrofitUtil.Results.Success -> {
                    _roomList.postValue(result.data)
                    _errorMessage.postValue(null)
                }
                is RetrofitUtil.Results.Error -> {
                    _errorMessage.postValue(result.message)
                }
            }
        }
    }

    fun getChatRoom(roomId: Int) {
        viewModelScope.launch {
            when (val result = repository.getChatMessages(roomId)) {
                is RetrofitUtil.Results.Success -> {
                    _chatRoomName.postValue(result.data.roomName)
                    _messageList.postValue(result.data.chatList.toMutableList())
                    _memberList.postValue(result.data.userList)
                    _errorMessage.postValue(null)
                }
                is RetrofitUtil.Results.Error -> {
                    _errorMessage.postValue(result.message)
                }
            }
        }
    }

    fun addMessage(dto : ChatMessageReceiveDto) {
        _messageList.value?.add(dto)
        _messageList.postValue(_messageList.value)
    }
}