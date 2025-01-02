package com.app.talkwave.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.talkwave.model.utils.RetrofitUtil
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> = _errorMessage

    protected fun <T> execute(call: suspend () -> RetrofitUtil.Results<T>, onSuccess: (T) -> Unit) {
        viewModelScope.launch {
            when (val result = call()) {
                is RetrofitUtil.Results.Success -> {
                    onSuccess(result.data)
                    _errorMessage.postValue(null)
                }
                is RetrofitUtil.Results.Error -> {
                    _errorMessage.postValue(result.message)
                }
            }
        }
    }
}