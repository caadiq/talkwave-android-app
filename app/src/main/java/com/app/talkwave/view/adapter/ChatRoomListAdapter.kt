package com.app.talkwave.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.talkwave.databinding.RowChatListBinding
import com.app.talkwave.model.dto.ChatRoomListDto
import com.app.talkwave.view.diff.ChatRoomListDiffUtil
import com.app.talkwave.view.utils.DateTimeConverter.formatChatRoomDateTime

class ChatRoomListAdapter : RecyclerView.Adapter<ChatRoomListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<ChatRoomListDto>()
    private var onItemClickListener: ((ChatRoomListDto, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowChatListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowChatListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: ChatRoomListDto) {
            binding.txtTitle.text = item.roomName
            binding.txtMessage.text = item.latestMessage
            binding.txtDate.text = formatChatRoomDateTime(item.sendDate)
        }
    }

    fun setOnItemClickListener(listener: (ChatRoomListDto, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<ChatRoomListDto>) {
        val diffCallBack = ChatRoomListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}