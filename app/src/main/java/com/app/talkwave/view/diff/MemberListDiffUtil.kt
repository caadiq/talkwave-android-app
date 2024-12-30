package com.app.talkwave.view.diff

import androidx.recyclerview.widget.DiffUtil
import com.app.talkwave.model.dto.MemberListDto

class MemberListDiffUtil(private val oldList: List<MemberListDto>, private val newList: List<MemberListDto>) : DiffUtil.Callback() {
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
        return oldList[oldItemPosition].userId == newList[newItemPosition].userId
    }
}