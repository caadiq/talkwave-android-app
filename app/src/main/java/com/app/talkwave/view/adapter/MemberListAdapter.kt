package com.app.talkwave.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.talkwave.databinding.RowChatMemberBinding
import com.app.talkwave.model.dto.MemberListDto
import com.app.talkwave.view.diff.MemberListDiffUtil

class MemberListAdapter : RecyclerView.Adapter<MemberListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<MemberListDto>()
    private var onItemClickListener: ((MemberListDto, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowChatMemberBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowChatMemberBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: MemberListDto) {
            binding.txtName.text = item.userName
        }
    }

    fun setOnItemClickListener(listener: (MemberListDto, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<MemberListDto>) {
        val diffCallBack = MemberListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}