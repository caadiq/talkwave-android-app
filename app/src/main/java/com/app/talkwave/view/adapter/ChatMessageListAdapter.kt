package com.app.talkwave.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.talkwave.databinding.RowChatMessageMeBinding
import com.app.talkwave.databinding.RowChatMessageYouBinding
import com.app.talkwave.model.dto.ChatMessageDto
import com.app.talkwave.view.diff.ChatMessageListDiffUtil
import com.app.talkwave.view.utils.DateTimeConverter.formatChatDateTime

class ChatMessageListAdapter(private val userId: String?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList = mutableListOf<ChatMessageDto>()

    companion object {
        private const val VIEW_TYPE_ME = 0
        private const val VIEW_TYPE_YOU = 1
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int = if (itemList[position].userId == userId) VIEW_TYPE_ME else VIEW_TYPE_YOU

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ME) {
            val binding = RowChatMessageMeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MeViewHolder(binding)
        } else {
            val binding = RowChatMessageYouBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            YouViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MeViewHolder) {
            holder.bind(itemList[position])
        } else if (holder is YouViewHolder) {
            holder.bind(itemList[position])
        }
    }

    inner class MeViewHolder(private val binding: RowChatMessageMeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatMessageDto) {
            binding.txtMessage.text = item.message
            binding.txtDate.text = formatChatDateTime(item.date)
        }
    }

    inner class YouViewHolder(private val binding: RowChatMessageYouBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatMessageDto) {
            binding.txtName.text = item.userName
            binding.txtMessage.text = item.message
            binding.txtDate.text = formatChatDateTime(item.date)
        }
    }

    fun setItemList(list: List<ChatMessageDto>) {
        val diffCallback = ChatMessageListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}