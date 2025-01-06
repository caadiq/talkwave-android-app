package com.app.talkwave.model.dto

data class DeptListDto(
    val deptId: Int,
    val name: String,
    val userInfoList: List<UserInfoList>
)

data class UserInfoList(
    val userName: String,
    val userId: String,
    var selected: Boolean = false
)
