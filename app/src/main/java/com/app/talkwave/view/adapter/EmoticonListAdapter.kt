package com.app.talkwave.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.talkwave.databinding.RowEmoticonBinding
import com.app.talkwave.model.dto.EmoticonListDto
import com.app.talkwave.view.diff.EmoticonListDiffUtil
import com.bumptech.glide.Glide

class EmoticonListAdapter : RecyclerView.Adapter<EmoticonListAdapter.ViewHolder>() {
    private var itemList = mutableListOf<EmoticonListDto>()
    private var onItemClickListener: ((EmoticonListDto, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowEmoticonBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowEmoticonBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: EmoticonListDto) {
            Glide.with(binding.root).load(item.url).into(binding.imgEmoticon)
        }
    }

    fun setOnItemClickListener(listener: (EmoticonListDto, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setItemList(list: List<EmoticonListDto>) {
        val diffCallBack = EmoticonListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}