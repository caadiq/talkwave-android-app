package com.app.talkwave.view.diff

import androidx.recyclerview.widget.DiffUtil
import com.app.talkwave.model.dto.ChatMessageReceiveDto

class ChatMessageListDiffUtil(private val oldList: List<ChatMessageReceiveDto>, private val newList: List<ChatMessageReceiveDto>) : DiffUtil.Callback() {
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