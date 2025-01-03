package com.app.talkwave.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.talkwave.databinding.RowDeptBinding
import com.app.talkwave.model.dto.DeptListDto
import com.app.talkwave.view.diff.DeptListDiffUtil

class DeptListAdapter : RecyclerView.Adapter<DeptListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<DeptListDto>()
    private var onItemClickListener: ((DeptListDto, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowDeptBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowDeptBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: DeptListDto) {
            binding.txtDept.text = item.name
        }
    }

    fun setOnItemClickListener(listener: (DeptListDto, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<DeptListDto>) {
        val diffCallBack = DeptListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}