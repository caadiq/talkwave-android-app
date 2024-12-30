package com.app.talkwave.view.view

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.app.talkwave.databinding.ActivityChatMessageBinding
import com.app.talkwave.model.dto.ChatMessageDto
import com.app.talkwave.model.dto.MemberListDto
import com.app.talkwave.view.adapter.ChatMessageListAdapter
import com.app.talkwave.view.adapter.MemberListAdapter

class ChatMessageActivity : AppCompatActivity() {
    private val binding by lazy { ActivityChatMessageBinding.inflate(layoutInflater) }

    private lateinit var chatMessageListAdapter: ChatMessageListAdapter
    private var memberListAdapter = MemberListAdapter()

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
    }

    private fun setupRecyclerView() {
        chatMessageListAdapter = ChatMessageListAdapter(this, "user1")
        binding.recyclerMessages.apply {
            adapter = chatMessageListAdapter
            itemAnimator = null
        }

        chatMessageListAdapter.setItemList(
            listOf(
                ChatMessageDto(
                    chatId = 1,
                    userId = "user2",
                    userName = "김철수",
                    message = "이번 주말에 축구 경기 있어요. 같이 보러 가실래요?",
                    date = "2024-12-04T01:45:00"
                ),
                ChatMessageDto(
                    chatId = 2,
                    userId = "user2",
                    userName = "김철수",
                    message = "스케이트 타러 가실래요? 좋은 장소를 찾았어요.",
                    date = "2024-12-05T04:00:00"
                ),
                ChatMessageDto(
                    chatId = 3,
                    userId = "user1",
                    userName = "홍길동",
                    message = "축구 경기 보셨나요? 정말 흥미진진했어요!",
                    date = "2024-12-06T06:15:00"
                ),
                ChatMessageDto(
                    chatId = 4,
                    userId = "user1",
                    userName = "홍길동",
                    message = "오늘 회의 잘 마쳤나요? 다음 주 일정 조율해봐요.",
                    date = "2024-12-07T08:30:00"
                ),
                ChatMessageDto(
                    chatId = 5,
                    userId = "user4",
                    userName = "박민수",
                    message = "안녕하세요! 오랜만에 연락드려요. 잘 지내고 계신가요?",
                    date = "2024-12-08T10:45:00"
                ),
                ChatMessageDto(
                    chatId = 6,
                    userId = "user4",
                    userName = "박민수",
                    message = "이번 주말에 시간 되세요? 같이 쇼핑 가요.",
                    date = "2024-12-09T13:00:00"
                ),
                ChatMessageDto(
                    chatId = 7,
                    userId = "user3",
                    userName = "이영희",
                    message = "영화 보러 가실래요? 좋은 영화가 개봉했어요.",
                    date = "2024-12-10T15:15:00"
                ),
                ChatMessageDto(
                    chatId = 8,
                    userId = "user3",
                    userName = "이영희",
                    message = "이번 주말에 여행 가실래요? 좋은 장소를 찾았어요.",
                    date = "2024-12-11T17:30:00"
                ),
                ChatMessageDto(
                    chatId = 9,
                    userId = "user2",
                    userName = "김철수",
                    message = "오늘 저녁에 시간 되세요? 같이 저녁 먹어요.",
                    date = "2024-12-12T19:45:00"
                ),
                ChatMessageDto(
                    chatId = 10,
                    userId = "user2",
                    userName = "김철수",
                    message = "안녕하세요, 오랜만이에요. 잘 지내고 계신가요?",
                    date = "2024-12-13T08:00:00"
                ),
                ChatMessageDto(
                    chatId = 11,
                    userId = "user1",
                    userName = "홍길동",
                    message = "이번 주말에 축구 경기 있어요. 같이 보러 가실래요?",
                    date = "2024-12-14T11:05:10"
                ),
                ChatMessageDto(
                    chatId = 12,
                    userId = "user1",
                    userName = "홍길동",
                    message = "스케이트 타러 가실래요? 좋은 장소를 찾았어요.",
                    date = "2024-12-15T14:10:20"
                ),
                ChatMessageDto(
                    chatId = 13,
                    userId = "user4",
                    userName = "박민수",
                    message = "축구 경기 보셨나요? 정말 흥미진진했어요!",
                    date = "2024-12-16T20:45:30"
                ),
                ChatMessageDto(
                    chatId = 14,
                    userId = "user4",
                    userName = "박민수",
                    message = "오늘 회의 잘 마쳤나요? 다음 주 일정 조율해봐요.",
                    date = "2024-12-17T09:15:45"
                ),
                ChatMessageDto(
                    chatId = 15,
                    userId = "user3",
                    userName = "이영희",
                    message = "안녕하세요! 오랜만에 연락드려요. 잘 지내고 계신가요?",
                    date = "2024-12-18T18:30:00"
                ),
                ChatMessageDto(
                    chatId = 16,
                    userId = "user3",
                    userName = "이영희",
                    message = "잘 지내요? 요즘 바쁘신가요? 시간이 되면 한번 만나서 이야기 나누고 싶어요. 연락 주세요!",
                    date = "2024-12-19T08:45:25"
                ),
                ChatMessageDto(
                    chatId = 17,
                    userId = "user2",
                    userName = "김철수",
                    message = "주말에 뭐해요? 저는 이번 주말에 가족들과 함께 시간을 보낼 예정이에요. 당신은 어떤 계획이 있나요?",
                    date = "2024-12-20T10:05:50"
                ),
                ChatMessageDto(
                    chatId = 18,
                    userId = "user2",
                    userName = "김철수",
                    message = "점심 뭐 먹었어요? 저는 오늘 새로운 식당에 가봤는데 정말 맛있었어요. 다음에 같이 가봐요!",
                    date = "2024-12-21T12:15:10"
                ),
                ChatMessageDto(
                    chatId = 19,
                    userId = "user1",
                    userName = "홍길동",
                    message = "오늘 날씨가 정말 좋네요. 주말에 산책이라도 가야겠어요. 혹시 같이 가실래요?",
                    date = "2024-12-22T14:20:30"
                ),
                ChatMessageDto(
                    chatId = 20,
                    userId = "user1",
                    userName = "홍길동",
                    message = "안녕하세요, 오랜만이에요. 잘 지내고 계신가요? 요즘 날씨가 많이 추워졌네요. 감기 조심하세요!",
                    date = "2024-12-23T16:39:45"
                ),
                ChatMessageDto(
                    chatId = 21,
                    userId = "user1",
                    userName = "홍길동",
                    message = "오늘 점심 뭐 드셨어요? 저는 샐러드 먹었어요.",
                    date = "2024-12-26T12:30:00"
                ),
                ChatMessageDto(
                    chatId = 22,
                    userId = "user2",
                    userName = "김철수",
                    message = "내일 회의 준비 잘 하고 계신가요?",
                    date = "2024-12-26T15:45:00"
                ),
                ChatMessageDto(
                    chatId = 23,
                    userId = "user3",
                    userName = "이영희",
                    message = "이번 주말에 영화 보러 가실래요?",
                    date = "2024-12-26T18:00:00"
                ),
                ChatMessageDto(
                    chatId = 24,
                    userId = "user4",
                    userName = "박민수",
                    message = "오늘 저녁에 시간 되세요? 같이 저녁 먹어요.",
                    date = "2024-12-26T20:15:00"
                ),
                ChatMessageDto(
                    chatId = 25,
                    userId = "user1",
                    userName = "홍길동",
                    message = "네",
                    date = "2024-12-27T09:00:00"
                )
            )
        )

        binding.recyclerMessages.scrollToPosition(chatMessageListAdapter.itemCount - 1)


        binding.recyclerMembers.apply {
            adapter = memberListAdapter
            setHasFixedSize(true)
            itemAnimator = null
        }

        memberListAdapter.setItemList(
            listOf(
                MemberListDto(
                    userId = "user1",
                    userName = "홍길동"
                ),
                MemberListDto(
                    userId = "user2",
                    userName = "김철수"
                ),
                MemberListDto(
                    userId = "user3",
                    userName = "이영희"
                ),
                MemberListDto(
                    userId = "user4",
                    userName = "박민수"
                ),
                MemberListDto(
                    userId = "user5",
                    userName = "이철민"
                ),
                MemberListDto(
                    userId = "user6",
                    userName = "김영수"
                ),
                MemberListDto(
                    userId = "user7",
                    userName = "박영희"
                ),
                MemberListDto(
                    userId = "user8",
                    userName = "이민수"
                ),
                MemberListDto(
                    userId = "user9",
                    userName = "김철민"
                ),
                MemberListDto(
                    userId = "user10",
                    userName = "박영수"
                ),
                MemberListDto(
                    userId = "user11",
                    userName = "이영희"
                ),
                MemberListDto(
                    userId = "user12",
                    userName = "김민수"
                ),
                MemberListDto(
                    userId = "user13",
                    userName = "박철민"
                ),
                MemberListDto(
                    userId = "user14",
                    userName = "이영수"
                ),
                MemberListDto(
                    userId = "user15",
                    userName = "김영희"
                ),
                MemberListDto(
                    userId = "user16",
                    userName = "박민수"
                ),
                MemberListDto(
                    userId = "user17",
                    userName = "이철민"
                ),
                MemberListDto(
                    userId = "user18",
                    userName = "김영수"
                ),
                MemberListDto(
                    userId = "user19",
                    userName = "박영희"
                ),
                MemberListDto(
                    userId = "user20",
                    userName = "이민수"
                )
            )
        )

        binding.txtMembers.text = memberListAdapter.itemCount.toString()
    }

    private fun back() {
        if (binding.editSearch.isFocused) {
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