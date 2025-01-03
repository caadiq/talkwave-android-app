package com.app.talkwave.viewmodel

import androidx.lifecycle.MutableLiveData
import com.app.talkwave.model.dto.DeptListDto
import com.app.talkwave.model.repository.DeptRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeptViewModel @Inject constructor(private val repository: DeptRepository) : BaseViewModel() {
    private val _deptList = MutableLiveData<List<DeptListDto>>()
    val deptList: MutableLiveData<List<DeptListDto>> = _deptList

    fun getDeptList() {
        execute(
            call = { repository.getDeptList() },
            onSuccess = { data -> _deptList.postValue(data) }
        )
    }
}