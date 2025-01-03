package com.app.talkwave.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.talkwave.model.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(private val repository: DataStoreRepository) : ViewModel() {
    val userId = repository.getUserId()
    val password = repository.getPassword()

    fun saveUserId(userId: String) {
        viewModelScope.launch {
            repository.saveUserId(userId)
        }
    }

    fun savePassword(password: String) {
        viewModelScope.launch {
            repository.savePassword(password)
        }
    }
}