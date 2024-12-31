package com.app.talkwave.view.view

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.talkwave.databinding.ActivityChatMessageBinding
import com.app.talkwave.model.dto.ChatMessageSendDto
import com.app.talkwave.model.service.StompClient
import com.app.talkwave.view.adapter.ChatMessageListAdapter
import com.app.talkwave.view.adapter.MemberListAdapter
import com.app.talkwave.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatMessageActivity : AppCompatActivity() {
    private val binding by lazy { ActivityChatMessageBinding.inflate(layoutInflater) }

    private val chatViewModel by viewModels<ChatViewModel>()

    private lateinit var chatMessageListAdapter: ChatMessageListAdapter
    private var memberListAdapter = MemberListAdapter()

    private val stompClient = StompClient()

    private val imm by lazy { getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager }

    private var searchResults = mutableListOf<Int>()
    private var currentSearchIndex = 0

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            back()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        setupView()
        setupRecyclerView()
        setupViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        stompClient.disconnect()
    }

    override fun onPause() {
        super.onPause()
        stompClient.disconnect()
    }

    override fun onResume() {
        super.onResume()
        setupChat()
    }

    private fun setupChat() {
        stompClient.apply {
            connect()
            subscribe(5) { chat ->
                chatViewModel.addMessage(chat)
            }
        }
    }

    private fun setupView() {
        binding.btnBack.setOnClickListener {
            back()
        }

        binding.imgSearch.setOnClickListener {
            binding.layoutToolbar.visibility = View.GONE
            binding.layoutMessage.visibility = View.GONE
            binding.layoutSearchHeader.visibility = View.VISIBLE
            binding.layoutSearchFooter.visibility = View.VISIBLE
            binding.editSearch.requestFocus()
            imm.showSoftInput(binding.editSearch, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.imgMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.navigationView)
        }

        binding.editSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchMessages(binding.editSearch.text.toString())
                imm.hideSoftInputFromWindow(binding.editSearch.windowToken, 0)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.imgClear.setOnClickListener {
            binding.editSearch.text?.clear()
        }

        binding.btnSearchPrev.setOnClickListener {
            if (searchResults.isNotEmpty()) {
                currentSearchIndex = (currentSearchIndex - 1 + searchResults.size) % searchResults.size
                scrollToSearchResult()
            }
        }

        binding.btnSearchNext.setOnClickListener {
            if (searchResults.isNotEmpty()) {
                currentSearchIndex = (currentSearchIndex + 1) % searchResults.size
                scrollToSearchResult()
            }
        }

        binding.btnSend.setOnClickListener {
            val message = binding.editMessage.text.toString()

            if (message.trim().isEmpty()) {
                return@setOnClickListener
            }

            stompClient.sendMessage(
                ChatMessageSendDto(
                    roomId = 5,
                    userId = "caadiq",
                    message = message
                )
            )
            binding.editMessage.text?.clear()
        }
    }

    private fun setupRecyclerView() {
        chatMessageListAdapter = ChatMessageListAdapter(this, "user1")
        binding.recyclerMessages.apply {
            adapter = chatMessageListAdapter
            itemAnimator = null
        }

        binding.recyclerMembers.apply {
            adapter = memberListAdapter
            setHasFixedSize(true)
            itemAnimator = null
        }

        binding.txtMembers.text = memberListAdapter.itemCount.toString()
    }

    private fun setupViewModel() {
        chatViewModel.messageList.observe(this) {
            chatMessageListAdapter.setItemList(it)
            binding.recyclerMessages.scrollToPosition(chatMessageListAdapter.itemCount - 1)
        }
    }

    private fun back() {
        if (binding.navigationView.isShown) {
            binding.drawerLayout.closeDrawer(binding.navigationView)
        } else if (binding.editSearch.isFocused) {
            binding.editSearch.clearFocus()
            binding.editSearch.text?.clear()
            binding.layoutToolbar.visibility = View.VISIBLE
            binding.layoutMessage.visibility = View.VISIBLE
            binding.layoutSearchHeader.visibility = View.GONE
            binding.layoutSearchFooter.visibility = View.GONE
            chatMessageListAdapter.setSearchQuery(null)
            searchResults.clear()
            currentSearchIndex = 0
            imm.hideSoftInputFromWindow(binding.editSearch.windowToken, 0)
        } else {
            finish()
        }
    }

    private fun searchMessages(query: String) {
        searchResults.clear()
        currentSearchIndex = 0

        chatMessageListAdapter.getItemList().forEachIndexed { index, chatMessageDto ->
            if (chatMessageDto.message.contains(query, ignoreCase = true)) {
                searchResults.add(index)
            }
        }
        if (searchResults.isNotEmpty()) {
            currentSearchIndex = searchResults.size - 1
            chatMessageListAdapter.setSearchQuery(query)
            scrollToSearchResult()
        } else {
            chatMessageListAdapter.setSearchQuery(null)
        }

        updateSearchButtons()
    }

    private fun scrollToSearchResult() {
        binding.recyclerMessages.smoothScrollToPosition(searchResults[currentSearchIndex])
        updateSearchButtons()
    }

    private fun updateSearchButtons() {
        binding.btnSearchPrev.apply {
            isEnabled = currentSearchIndex > 0
            alpha = if (currentSearchIndex > 0) 1.0f else 0.5f
        }

        binding.btnSearchNext.apply {
            isEnabled = currentSearchIndex < searchResults.size - 1
            alpha = if (currentSearchIndex < searchResults.size - 1) 1.0f else 0.5f
        }
    }
}