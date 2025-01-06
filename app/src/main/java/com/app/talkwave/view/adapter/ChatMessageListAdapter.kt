package com.app.talkwave.view.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.talkwave.R
import com.app.talkwave.databinding.RowChatMessageMeBinding
import com.app.talkwave.databinding.RowChatMessageYouBinding
import com.app.talkwave.model.dto.ChatMessageReceiveDto
import com.app.talkwave.view.diff.ChatMessageListDiffUtil
import com.app.talkwave.view.utils.DateTimeConverter.convertDateTime

class ChatMessageListAdapter(private val context: Context, private val userId: String?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList = mutableListOf<ChatMessageReceiveDto>()
    private var searchQuery: String? = null
    private var currentSearchPosition: Int? = null

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
            holder.bind(itemList[position], searchQuery, position == currentSearchPosition)
        } else if (holder is YouViewHolder) {
            holder.bind(itemList[position], searchQuery, position == currentSearchPosition)
        }
    }

    inner class MeViewHolder(private val binding: RowChatMessageMeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatMessageReceiveDto, query: String?, isCurrentSearchPosition: Boolean) {
            binding.txtMessage.text = highlightQuery(item.message, query)
            binding.txtDate.text = convertDateTime(item.sendDate, "a h:mm")
            binding.txtFirstMessage.apply {
                visibility = if (item.isFirstMessage) View.VISIBLE else View.GONE
                text = convertDateTime(item.sendDate, "yyyy년 M월 d일 E요일")
            }
            if (isCurrentSearchPosition) {
                shakeView(binding.txtMessage)
            }
        }
    }

    inner class YouViewHolder(private val binding: RowChatMessageYouBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatMessageReceiveDto, query: String?, isCurrentSearchPosition: Boolean) {
            binding.txtName.text = item.userName
            binding.txtMessage.text = highlightQuery(item.message, query)
            binding.txtDate.text = convertDateTime(item.sendDate, "a h:mm")
            binding.txtFirstMessage.apply {
                visibility = if (item.isFirstMessage) View.VISIBLE else View.GONE
                text = convertDateTime(item.sendDate, "yyyy년 M월 d일 E요일")
            }
            if (isCurrentSearchPosition) {
                shakeView(binding.txtMessage)
            }
        }
    }

    fun setItemList(list: List<ChatMessageReceiveDto>) {
        val diffCallback = ChatMessageListDiffUtil(itemList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getItemList(): List<ChatMessageReceiveDto> = itemList

    fun setSearchQuery(query: String?) {
        searchQuery = query
        notifyItemRangeChanged(0, itemCount)
    }

    fun setCurrentSearchPosition(position: Int?) {
        currentSearchPosition = position
        notifyItemRangeChanged(0, itemCount)
    }

    private fun highlightQuery(text: String, query: String?): SpannableString {
        val spannableString = SpannableString(text)
        if (!query.isNullOrEmpty()) {
            var startIndex = text.indexOf(query, ignoreCase = true)
            while (startIndex >= 0) {
                spannableString.setSpan(
                    BackgroundColorSpan(context.resources.getColor(R.color.yellow, null)),
                    startIndex,
                    startIndex + query.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                startIndex = text.indexOf(query, startIndex + query.length, ignoreCase = true)
            }
        }
        return spannableString
    }

    private fun shakeView(view: View) {
        val animator = ObjectAnimator.ofFloat(view, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
        animator.duration = 500
        animator.start()
    }
}