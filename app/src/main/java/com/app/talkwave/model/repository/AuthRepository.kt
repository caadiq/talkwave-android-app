package com.app.talkwave.model.repository

import com.app.talkwave.model.api.AuthApi
import com.app.talkwave.model.dto.SignUpDto
import com.app.talkwave.model.utils.RetrofitUtil
import retrofit2.Retrofit
import javax.inject.Inject

class AuthRepository @Inject constructor(retrofit: Retrofit) {
    private val api: AuthApi = retrofit.create(AuthApi::class.java)

    suspend fun signUp(dto: SignUpDto): RetrofitUtil.Results<Unit> {
        return RetrofitUtil.call(api.signUp(dto))
    }

    suspend fun signIn(userId: String, password: String): RetrofitUtil.Results<Unit> {
        return RetrofitUtil.call(api.signIn(userId, password))
    }
}