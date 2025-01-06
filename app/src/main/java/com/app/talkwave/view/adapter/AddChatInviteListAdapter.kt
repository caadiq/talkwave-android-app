package com.app.talkwave.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.talkwave.databinding.RowAddChatInviteBinding
import com.app.talkwave.model.dto.UserInfoList
import com.app.talkwave.view.diff.UserInfoListDiffUtil


class AddChatInviteListAdapter : RecyclerView.Adapter<AddChatInviteListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<UserInfoList>()

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowAddChatInviteBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowAddChatInviteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserInfoList) {
            binding.txtName.text = item.userName
        }
    }

    fun setItemList(list: List<UserInfoList>) {
        val diffCallBack = UserInfoListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}