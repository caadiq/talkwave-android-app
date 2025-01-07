package com.app.talkwave.model.repository

import com.app.talkwave.model.data.DataStoreModule
import javax.inject.Inject

class DataStoreRepository @Inject constructor(private val dataStore: DataStoreModule) {
    suspend fun saveUserId(userId: String) {
        dataStore.saveUserId(userId)
    }

    fun getUserId() = dataStore.getUserId()

    suspend fun savePassword(password: String) {
        dataStore.savePassword(password)
    }

    fun getPassword() = dataStore.getPassword()

    suspend fun clearUserId() {
        dataStore.clearUserId()
    }

    suspend fun clearPassword() {
        dataStore.clearPassword()
    }
}