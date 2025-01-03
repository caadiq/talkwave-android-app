package com.app.talkwave.model.repository

import com.app.talkwave.model.api.DeptApi
import com.app.talkwave.model.dto.DeptListDto
import com.app.talkwave.model.utils.RetrofitUtil
import retrofit2.Retrofit
import javax.inject.Inject

class DeptRepository @Inject constructor(retrofit: Retrofit) {
    private val api: DeptApi = retrofit.create(DeptApi::class.java)

    suspend fun getDeptList(): RetrofitUtil.Results<List<DeptListDto>> {
        return RetrofitUtil.call(api.getDeptList())
    }
}