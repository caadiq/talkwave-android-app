package com.app.talkwave.viewmodel

import androidx.lifecycle.MutableLiveData
import com.app.talkwave.model.dto.SignUpDto
import com.app.talkwave.model.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository) : BaseViewModel(){
    private val _signUp = MutableLiveData<Unit>()
    val signUp: MutableLiveData<Unit> = _signUp

    private val _signIn = MutableLiveData<Unit>()
    val signIn: MutableLiveData<Unit> = _signIn

    private val _autoSignIn = MutableLiveData<Unit>()
    val autoSignIn: MutableLiveData<Unit> = _autoSignIn

    fun signUp(dto: SignUpDto) {
        execute(
            call = { repository.signUp(dto) },
            onSuccess = { _signUp.postValue(Unit) }
        )
    }

    fun signIn(userId: String, password: String) {
        execute(
            call = { repository.signIn(userId, password) },
            onSuccess = { _signIn.postValue(Unit) }
        )
    }

    fun autoSignIn(userId: String, password: String) {
        execute(
            call = { repository.signIn(userId, password) },
            onSuccess = { _autoSignIn.postValue(Unit) }
        )
    }
}