package com.app.talkwave.model.dto

data class SignUpDto(
    val userId: String,
    val password: String,
    val name: String,
    val deptId: Int
)
