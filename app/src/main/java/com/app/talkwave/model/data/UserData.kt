package com.app.talkwave.model.data

object UserData {
    private var userId: String? = null
    private var password: String? = null

    fun setUserData(userId: String, password: String) {
        this.userId = userId
        this.password = password
    }

    fun clearUserData() {
        this.userId = null
        this.password = null
    }
}