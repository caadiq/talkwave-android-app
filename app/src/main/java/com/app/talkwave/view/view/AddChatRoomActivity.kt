package com.app.talkwave.view.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.app.talkwave.R
import com.app.talkwave.databinding.ActivityAddChatRoomBinding
import com.app.talkwave.model.data.UserData
import com.app.talkwave.model.dto.ChatRoomAddDto
import com.app.talkwave.model.dto.UserInfoList
import com.app.talkwave.view.adapter.AddChatDeptListAdapter
import com.app.talkwave.view.adapter.AddChatInviteListAdapter
import com.app.talkwave.viewmodel.ChatViewModel
import com.app.talkwave.viewmodel.DeptViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddChatRoomActivity : AppCompatActivity(), AddChatDeptListAdapter.OnItemClickListener {
    private val binding by lazy { ActivityAddChatRoomBinding.inflate(layoutInflater) }

    private val deptViewModel by viewModels<DeptViewModel>()
    private val chatViewModel by viewModels<ChatViewModel>()

    private val addChatInviteListAdapter = AddChatInviteListAdapter()
    private val addChatDeptListAdapter = AddChatDeptListAdapter(this)

    private val inviteList = mutableListOf<UserInfoList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupRecyclerView()
        setupViewModel()
    }

    override fun onItemClick(item: UserInfoList) {
        if (item.selected) inviteList.add(0, item) else inviteList.remove(item)
        addChatInviteListAdapter.setItemList(inviteList)
        binding.recyclerInvite.scrollToPosition(0)

        val transition = AutoTransition().apply { duration = 200 }
        TransitionManager.beginDelayedTransition(binding.layoutBody, transition)
        binding.recyclerInvite.visibility = if (inviteList.isEmpty()) View.GONE else View.VISIBLE
        binding.txtCount.apply {
            text = inviteList.size.toString()
            visibility = if (inviteList.isEmpty()) View.GONE else View.VISIBLE
        }
        binding.txtConfirm.apply {
            isEnabled = inviteList.isNotEmpty()
            setTextColor(
                if (inviteList.isNotEmpty())
                    ContextCompat.getColor(this@AddChatRoomActivity, R.color.dark_gray)
                else
                    ContextCompat.getColor(this@AddChatRoomActivity, R.color.light_gray)
            )
        }
    }

    private fun setupView() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.txtConfirm.setOnClickListener {
            val roomName = binding.editRoomName.text?.trim().toString()

            if (roomName.isEmpty()) {
                Toast.makeText(this, "채팅방 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            DefaultDialog(
                title = null,
                message = "채팅방을 생성하시겠습니까?",
                onConfirm = {
                    UserData.getUserId()?.let {
                        chatViewModel.addChatRoom(
                            ChatRoomAddDto(
                                roomName = roomName,
                                userList = inviteList.map { user -> user.userId },
                                userId = it
                            )
                        )
                    }
                }
            ).show(supportFragmentManager, "DefaultDialog")
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerInvite.apply {
            adapter = addChatInviteListAdapter
            setHasFixedSize(true)
        }

        binding.recyclerUsers.adapter = addChatDeptListAdapter
    }

    private fun setupViewModel() {
        deptViewModel.apply {
            getDeptList()

            deptList.observe(this@AddChatRoomActivity) { list ->
                val myId = UserData.getUserId()
                val filteredList = list.map { dept ->
                    dept.copy(userInfoList = dept.userInfoList.filter { it.userId != myId })
                }
                addChatDeptListAdapter.setItemList(filteredList)
            }
        }

        chatViewModel.apply {
            addChatRoom.observe(this@AddChatRoomActivity) {
                Toast.makeText(this@AddChatRoomActivity, "채팅방이 생성되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}