package com.app.talkwave.view.diff

import androidx.recyclerview.widget.DiffUtil
import com.app.talkwave.model.dto.ChatMessageDto

class ChatMessageListDiffUtil(private val oldList: List<ChatMessageDto>, private val newList: List<ChatMessageDto>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].chatId == newList[newItemPosition].chatId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].chatId == newList[newItemPosition].chatId
    }
}