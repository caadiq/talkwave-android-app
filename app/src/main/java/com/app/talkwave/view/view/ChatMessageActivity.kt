package com.app.talkwave.view.view

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.app.talkwave.R
import com.app.talkwave.databinding.ActivityChatMessageBinding
import com.app.talkwave.model.data.UserData
import com.app.talkwave.model.dto.ChatMessageSendDto
import com.app.talkwave.model.dto.ChatRoomLeaveDto
import com.app.talkwave.model.service.StompClient
import com.app.talkwave.view.adapter.ChatMessageListAdapter
import com.app.talkwave.view.adapter.MemberListAdapter
import com.app.talkwave.view.utils.DateTimeConverter.convertDateTime
import com.app.talkwave.view.utils.InsetsWithKeyboardAnimationCallback
import com.app.talkwave.view.utils.InsetsWithKeyboardCallback
import com.app.talkwave.viewmodel.ChatViewModel
import com.bumptech.glide.Glide
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
    private var isAtBottom = true
    private var selectedEmoticon: Int? = null

    private val roomId by lazy { intent.getIntExtra("roomId", 0) }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            back()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        setupKeyboard()
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
        chatViewModel.getChatRoom(roomId)
        setupChat()
    }

    private fun setupChat() {
        stompClient.apply {
            connect()
            subscribe(roomId) { chat ->
                chatViewModel.addMessage(chat)
            }
        }
    }

    private fun setupKeyboard() {
        val insetsWithKeyboardCallback1 = InsetsWithKeyboardCallback(window)
        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutParent, insetsWithKeyboardCallback1)
        ViewCompat.setWindowInsetsAnimationCallback(binding.layoutParent, insetsWithKeyboardCallback1)

        val insetsWithKeyboardCallback2 = InsetsWithKeyboardCallback(window)
        ViewCompat.setOnApplyWindowInsetsListener(binding.navigationView, insetsWithKeyboardCallback2)
        ViewCompat.setWindowInsetsAnimationCallback(binding.navigationView, insetsWithKeyboardCallback2)

        listOf(
            binding.recyclerMessages,
            binding.layoutFooter,
            binding.layoutEmoticon,
            binding.btnExit
        ).forEach { view ->
            val callback = InsetsWithKeyboardAnimationCallback(view)
            ViewCompat.setWindowInsetsAnimationCallback(view, callback)
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
                if (binding.editSearch.text.toString().trim().isEmpty()) {
                    Toast.makeText(this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnEditorActionListener true
                }

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

        binding.btnExit.setOnClickListener {
            DefaultDialog(
                title = binding.txtName.text.toString(),
                message = "채팅방을 나가시겠습니까?",
                onConfirm = {
                    UserData.getUserId()?.let {
                        chatViewModel.leaveChatRoom(
                            ChatRoomLeaveDto(
                                roomId = roomId,
                                userId = it
                            )
                        )
                    }
                }
            ).show(supportFragmentManager, "DefaultDialog")
        }

        binding.layoutLastMessage.setOnClickListener {
            binding.recyclerMessages.scrollToPosition(chatMessageListAdapter.itemCount - 1)
        }

        binding.editMessage.addTextChangedListener {
            updateSendButton()
        }

        binding.btnSend.isEnabled = false
        binding.btnSend.setOnClickListener {
            val message = binding.editMessage.text.toString()

            if (message.trim().isEmpty() && selectedEmoticon == null) {
                return@setOnClickListener
            }

            UserData.getUserId()?.let {
                stompClient.sendMessage(
                    ChatMessageSendDto(
                        roomId = roomId,
                        userId = it,
                        message = message,
                        emojiId = selectedEmoticon
                    )
                )
            }
            binding.editMessage.text?.clear()
            binding.layoutEmoticon.visibility = View.GONE
            Glide.with(this).clear(binding.imgEmoticon)
            selectedEmoticon = null
            updateSendButton()
        }

        binding.fabScrollDown.setOnClickListener {
            binding.recyclerMessages.scrollToPosition(chatMessageListAdapter.itemCount - 1)
        }

        binding.btnEmoticon.setOnClickListener {
            chatViewModel.getEmojiList()
        }

        binding.imgClose.setOnClickListener {
            binding.layoutEmoticon.visibility = View.GONE
            Glide.with(this).clear(binding.imgEmoticon)
            selectedEmoticon = null
            updateSendButton()
        }
    }

    private fun setupRecyclerView() {
        chatMessageListAdapter = ChatMessageListAdapter(this, UserData.getUserId())

        binding.recyclerMessages.apply {
            adapter = chatMessageListAdapter
            itemAnimator = null

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    isAtBottom = !recyclerView.canScrollVertically(1)
                    if (recyclerView.canScrollVertically(1)) {
                        binding.fabScrollDown.show()
                    } else {
                        binding.fabScrollDown.hide()
                        if (binding.layoutLastMessage.isShown)
                            endMessageAnimation()
                    }
                }
            })
        }

        binding.recyclerMembers.apply {
            adapter = memberListAdapter
            itemAnimator = null
        }
    }

    private fun setupViewModel() {
        chatViewModel.apply {
            chatRoomName.observe(this@ChatMessageActivity) { roomName ->
                binding.txtName.text = roomName
            }

            memberList.observe(this@ChatMessageActivity) { list ->
                binding.txtMembers.text = list.size.toString()
                memberListAdapter.setItemList(list)
            }

            messageList.observe(this@ChatMessageActivity) { list ->
                if (!list.isNullOrEmpty()) {
                    var lastDate: String? = null
                    list.forEach { message ->
                        val messageDate = convertDateTime(message.sendDate, "yyyy-MM-dd")
                        if (messageDate != lastDate) {
                            message.isFirstMessage = true
                            lastDate = messageDate
                        } else {
                            message.isFirstMessage = false
                        }
                    }
                    chatMessageListAdapter.setItemList(list)

                    val lastMessage = list.last()
                    binding.txtLMName.text = lastMessage.userName
                    binding.txtLMMessage.text = if (lastMessage.message.isEmpty() && !lastMessage.emojiUrl.isNullOrEmpty()) "이모티콘" else lastMessage.message

                    if (isAtBottom) {
                        binding.recyclerMessages.scrollToPosition(chatMessageListAdapter.itemCount - 1)
                        binding.layoutLastMessage.visibility = View.GONE
                    } else if (lastMessage.userId == UserData.getUserId()) {
                        binding.recyclerMessages.scrollToPosition(chatMessageListAdapter.itemCount - 1)
                    } else {
                        if (lastMessage.userId != UserData.getUserId())
                            startMessageAnimation()
                    }
                }
            }

            emojiList.observe(this@ChatMessageActivity) { list ->
                EmoticonBottomSheetDialog(
                    list = list,
                    onItemClick = { item ->
                        selectedEmoticon = item.id
                        Glide.with(this@ChatMessageActivity).load(item.url).into(binding.imgEmoticon)
                        binding.layoutEmoticon.visibility = View.VISIBLE
                        updateSendButton()
                    }
                ).show(supportFragmentManager, "DeptBottomSheetDialog")
            }

            leaveChatRoom.observe(this@ChatMessageActivity) {
                finish()
            }
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
            chatMessageListAdapter.setCurrentSearchPosition(null)
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
            Toast.makeText(this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
        }

        updateSearchButtons()
    }

    private fun scrollToSearchResult() {
        val position = searchResults[currentSearchIndex]
        chatMessageListAdapter.setCurrentSearchPosition(position)
        binding.recyclerMessages.scrollToPosition(position)
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

    private fun updateSendButton() {
        binding.btnSend.isEnabled = binding.editMessage.text.toString().trim().isNotEmpty() || selectedEmoticon != null
    }

    private fun startMessageAnimation() {
        val fadeIn = AnimationUtils.loadAnimation(this@ChatMessageActivity, R.anim.fade_in)
        binding.layoutLastMessage.startAnimation(fadeIn)
        binding.layoutLastMessage.visibility = View.VISIBLE
    }

    private fun endMessageAnimation() {
        val fadeOut = AnimationUtils.loadAnimation(this@ChatMessageActivity, R.anim.fade_out)
        binding.layoutLastMessage.startAnimation(fadeOut)
        binding.layoutLastMessage.visibility = View.GONE
    }
}