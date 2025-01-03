package com.app.talkwave.model.api

import com.app.talkwave.model.dto.DeptListDto
import retrofit2.Call
import retrofit2.http.GET

interface DeptApi {

    @GET("/api/talkwave/dept")
    fun getDeptList(): Call<List<DeptListDto>>
}