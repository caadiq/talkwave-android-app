package com.app.talkwave.model.api

import com.app.talkwave.model.dto.SignUpDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("/api/talkwave/user/register")
    fun signUp(@Body dto: SignUpDto): Call<Unit>

    @GET("/api/talkwave/user/login")
    fun signIn(
        @Query("userId") userId: String,
        @Query("password") password: String
    ): Call<Unit>
}