package com.app.talkwave.model.data

object UserData {
    private var userId: String? = null

    fun setUserId(userId: String) {
        this.userId = userId
    }

    fun getUserId(): String? {
        return userId
    }

    fun clearUserId() {
        this.userId = null
    }
}