package com.app.talkwave.view.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.talkwave.databinding.FragmentChatBinding
import com.app.talkwave.model.dto.ChatListDto
import com.app.talkwave.view.adapter.ChatListAdapter

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val chatListAdapter = ChatListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.recyclerView.apply {
            adapter = chatListAdapter
            setHasFixedSize(true)
        }

        chatListAdapter.setItemList(
            listOf(
                ChatListDto(
                    chatId = 1,
                    title = "홍길동",
                    message = "안녕하세요, 오랜만이에요. 잘 지내고 계신가요? 요즘 날씨가 많이 추워졌네요. 감기 조심하세요!",
                    date = "2024-12-23T16:39:45"
                ),
                ChatListDto(
                    chatId = 2,
                    title = "김철수",
                    message = "오늘 날씨가 정말 좋네요. 주말에 산책이라도 가야겠어요. 혹시 같이 가실래요?",
                    date = "2024-12-22T14:20:30"
                ),
                ChatListDto(
                    chatId = 3,
                    title = "이영희",
                    message = "점심 뭐 먹었어요? 저는 오늘 새로운 식당에 가봤는데 정말 맛있었어요. 다음에 같이 가봐요!",
                    date = "2024-12-21T12:15:10"
                ),
                ChatListDto(
                    chatId = 4,
                    title = "박민수",
                    message = "주말에 뭐해요? 저는 이번 주말에 가족들과 함께 시간을 보낼 예정이에요. 당신은 어떤 계획이 있나요?",
                    date = "2024-12-20T10:05:50"
                ),
                ChatListDto(
                    chatId = 5,
                    title = "최지우",
                    message = "잘 지내요? 요즘 바쁘신가요? 시간이 되면 한번 만나서 이야기 나누고 싶어요. 연락 주세요!",
                    date = "2024-12-19T08:45:25"
                ),
                ChatListDto(
                    chatId = 6,
                    title = "한지민",
                    message = "안녕하세요! 오랜만에 연락드려요. 잘 지내고 계신가요?",
                    date = "2024-12-18T18:30:00"
                ),
                ChatListDto(
                    chatId = 7,
                    title = "이준호",
                    message = "오늘 회의 잘 마쳤나요? 다음 주 일정 조율해봐요.",
                    date = "2024-12-17T09:15:45"
                ),
                ChatListDto(
                    chatId = 8,
                    title = "박지성",
                    message = "축구 경기 보셨나요? 정말 흥미진진했어요!",
                    date = "2024-12-16T20:45:30"
                ),
                ChatListDto(
                    chatId = 9,
                    title = "김연아",
                    message = "스케이트 타러 가실래요? 좋은 장소를 찾았어요.",
                    date = "2024-12-15T14:10:20"
                ),
                ChatListDto(
                    chatId = 10,
                    title = "손흥민",
                    message = "이번 주말에 축구 경기 있어요. 같이 보러 가실래요?",
                    date = "2024-12-14T11:05:10"
                ),
                ChatListDto(
                    chatId = 11,
                    title = "유재석",
                    message = "안녕하세요, 오랜만이에요. 잘 지내고 계신가요?",
                    date = "2024-12-13T08:00:00"
                ),
                ChatListDto(
                    chatId = 12,
                    title = "강호동",
                    message = "오늘 저녁에 시간 되세요? 같이 저녁 먹어요.",
                    date = "2024-12-12T19:45:00"
                ),
                ChatListDto(
                    chatId = 13,
                    title = "이효리",
                    message = "이번 주말에 여행 가실래요? 좋은 장소를 찾았어요.",
                    date = "2024-12-11T17:30:00"
                ),
                ChatListDto(
                    chatId = 14,
                    title = "정우성",
                    message = "영화 보러 가실래요? 좋은 영화가 개봉했어요.",
                    date = "2024-12-10T15:15:00"
                ),
                ChatListDto(
                    chatId = 15,
                    title = "김혜수",
                    message = "이번 주말에 시간 되세요? 같이 쇼핑 가요.",
                    date = "2024-12-09T13:00:00"
                ),
                ChatListDto(
                    chatId = 16,
                    title = "송중기",
                    message = "안녕하세요! 오랜만에 연락드려요. 잘 지내고 계신가요?",
                    date = "2024-12-08T10:45:00"
                ),
                ChatListDto(
                    chatId = 17,
                    title = "전지현",
                    message = "오늘 회의 잘 마쳤나요? 다음 주 일정 조율해봐요.",
                    date = "2024-12-07T08:30:00"
                ),
                ChatListDto(
                    chatId = 18,
                    title = "이병헌",
                    message = "축구 경기 보셨나요? 정말 흥미진진했어요!",
                    date = "2024-12-06T06:15:00"
                ),
                ChatListDto(
                    chatId = 19,
                    title = "김수현",
                    message = "스케이트 타러 가실래요? 좋은 장소를 찾았어요.",
                    date = "2024-12-05T04:00:00"
                ),
                ChatListDto(
                    chatId = 20,
                    title = "박보검",
                    message = "이번 주말에 축구 경기 있어요. 같이 보러 가실래요?",
                    date = "2024-12-04T01:45:00"
                )
            )
        )

        chatListAdapter.setOnItemClickListener { item, _ ->
            startActivity(Intent(requireContext(), ChatMessageActivity::class.java))
        }
    }
}