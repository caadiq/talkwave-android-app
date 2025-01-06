package com.app.talkwave.view.diff

import androidx.recyclerview.widget.DiffUtil
import com.app.talkwave.model.dto.UserInfoList

class UserInfoListDiffUtil(private val oldList: List<UserInfoList>, private val newList: List<UserInfoList>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].userId == newList[newItemPosition].userId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}