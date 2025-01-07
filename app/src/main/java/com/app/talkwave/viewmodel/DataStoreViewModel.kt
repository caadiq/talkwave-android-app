package com.app.talkwave.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.app.talkwave.model.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(private val repository: DataStoreRepository) : ViewModel() {
    val userId = repository.getUserId().asLiveData()
    val password = repository.getPassword().asLiveData()

    val userIdFlow = repository.getUserId()
    val passwordFlow = repository.getPassword()

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

    fun clearUserId() {
        viewModelScope.launch {
            repository.clearUserId()
        }
    }

    fun clearPassword() {
        viewModelScope.launch {
            repository.clearPassword()
        }
    }

    fun <A, B> combine(liveData1: LiveData<A>, liveData2: LiveData<B>): LiveData<Pair<A?, B?>> {
        return MediatorLiveData<Pair<A?, B?>>().apply {
            addSource(liveData1) { value = it to liveData2.value }
            addSource(liveData2) { value = liveData1.value to it }
        }
    }
}