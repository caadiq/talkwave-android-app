package com.app.talkwave.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class MainFragmentType(val tag: String) {
    HOME("home"),
    CHAT("chat"),
    PROFILE("profile"),
    SETTINGS("settings")
}

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _currentFragmentType = MutableLiveData(MainFragmentType.HOME)
    val currentFragmentType: LiveData<MainFragmentType> = _currentFragmentType

    fun setCurrentFragment(item: Int): Boolean {
        val pageType = getPageType(item)
        changeCurrentFragmentType(pageType)

        return true
    }

    private fun getPageType(item: Int): MainFragmentType {
        return when (item) {
            0 -> MainFragmentType.HOME
            1 -> MainFragmentType.CHAT
            2 -> MainFragmentType.PROFILE
            3 -> MainFragmentType.SETTINGS
            else -> MainFragmentType.HOME
        }
    }

    private fun changeCurrentFragmentType(fragmentType: MainFragmentType) {
        if (currentFragmentType.value == fragmentType)
            return

        _currentFragmentType.value = fragmentType
    }
}