package com.app.talkwave.view.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.app.talkwave.databinding.FragmentChatBinding
import com.app.talkwave.model.data.UserData
import com.app.talkwave.view.adapter.ChatRoomListAdapter
import com.app.talkwave.viewmodel.ChatViewModel

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val chatViewModel by activityViewModels<ChatViewModel>()

    private val chatRoomListAdapter = ChatRoomListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            UserData.getUserId()?.let { chatViewModel.getRoomList(it) }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = chatRoomListAdapter
            itemAnimator = null
            setHasFixedSize(true)
        }

        chatRoomListAdapter.setOnItemClickListener { item, _ ->
            val intent = Intent(requireContext(), ChatMessageActivity::class.java)
            intent.putExtra("roomId", item.roomId)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        chatViewModel.apply {
            roomList.observe(viewLifecycleOwner) { list ->
                chatRoomListAdapter.setItemList(list)
            }
        }
    }
}