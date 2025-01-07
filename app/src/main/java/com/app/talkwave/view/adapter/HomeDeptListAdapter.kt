package com.app.talkwave.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.talkwave.databinding.RowHomeDeptBinding
import com.app.talkwave.model.dto.DeptListDto
import com.app.talkwave.view.diff.DeptListDiffUtil

class HomeDeptListAdapter : RecyclerView.Adapter<HomeDeptListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<DeptListDto>()

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowHomeDeptBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowHomeDeptBinding) : RecyclerView.ViewHolder(binding.root) {
        private val homeUserListAdapter = HomeUserListAdapter()

        init {
            binding.recyclerView.apply {
                adapter = homeUserListAdapter
                itemAnimator = null
                setHasFixedSize(true)
            }
        }

        fun bind(item: DeptListDto) {
            binding.txtDept.text = item.name
            homeUserListAdapter.setItemList(item.userInfoList)
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