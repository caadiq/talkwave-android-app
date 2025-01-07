package com.app.talkwave.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.talkwave.databinding.RowHomeUserBinding
import com.app.talkwave.model.dto.UserInfoList
import com.app.talkwave.view.diff.UserInfoListDiffUtil

class HomeUserListAdapter : RecyclerView.Adapter<HomeUserListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<UserInfoList>()
    private var onItemClickListener: ((UserInfoList, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowHomeUserBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowHomeUserBinding) : RecyclerView.ViewHolder(binding.root) {
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
}