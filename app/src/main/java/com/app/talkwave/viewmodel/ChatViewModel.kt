package com.app.talkwave.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.talkwave.model.dto.ChatMessageReceiveDto
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    private val _messageList = MutableLiveData<MutableList<ChatMessageReceiveDto>>()
    val messageList: MutableLiveData<MutableList<ChatMessageReceiveDto>> = _messageList

    fun addMessage(dto : ChatMessageReceiveDto) {
        _messageList.value?.add(dto)
        _messageList.postValue(_messageList.value)
    }
}