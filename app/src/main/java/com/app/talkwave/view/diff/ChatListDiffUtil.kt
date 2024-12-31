package com.app.talkwave.view.diff

import androidx.recyclerview.widget.DiffUtil
import com.app.talkwave.model.dto.ChatListDto

class ChatListDiffUtil(private val oldList: List<ChatListDto>, private val newList: List<ChatListDto>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].roomId == newList[newItemPosition].roomId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}