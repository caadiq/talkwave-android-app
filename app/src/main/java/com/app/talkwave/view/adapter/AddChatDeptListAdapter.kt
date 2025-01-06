package com.app.talkwave.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.talkwave.databinding.RowAddChatDeptBinding
import com.app.talkwave.model.dto.DeptListDto
import com.app.talkwave.model.dto.UserInfoList
import com.app.talkwave.view.diff.DeptListDiffUtil

class AddChatDeptListAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<AddChatDeptListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(item: UserInfoList)
    }

    private var itemList = mutableListOf<DeptListDto>()

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowAddChatDeptBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowAddChatDeptBinding) : RecyclerView.ViewHolder(binding.root) {
        private val addChatUserListAdapter = AddChatUserListAdapter()

        init {
            binding.recyclerView.apply {
                adapter = addChatUserListAdapter
                itemAnimator = null
                setHasFixedSize(true)
            }
        }

        fun bind(item: DeptListDto) {
            binding.txtDept.text = item.name

            addChatUserListAdapter.apply {
                setItemList(item.userInfoList)
                setOnItemClickListener { item, position ->
                    selectItem(position)
                    listener.onItemClick(item)
                }
            }
        }
    }

    fun setItemList(list: List<DeptListDto>) {
        val diffCallBack = DeptListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}