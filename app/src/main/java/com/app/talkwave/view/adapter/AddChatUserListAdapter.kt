package com.app.talkwave.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.talkwave.databinding.RowAddChatUserBinding
import com.app.talkwave.model.dto.UserInfoList
import com.app.talkwave.view.diff.UserInfoListDiffUtil

class AddChatUserListAdapter : RecyclerView.Adapter<AddChatUserListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<UserInfoList>()
    private var onItemClickListener: ((UserInfoList, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowAddChatUserBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowAddChatUserBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: UserInfoList) {
            binding.txtName.text = item.userName
            binding.chkSelect.isChecked = item.selected
        }
    }

    fun setOnItemClickListener(listener: (UserInfoList, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<UserInfoList>) {
        val diffCallBack = UserInfoListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun selectItem(position: Int) {
        itemList[position].selected = !itemList[position].selected
        notifyItemChanged(position)
    }
}