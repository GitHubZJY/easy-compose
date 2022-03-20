package com.zjy.easy_compose.sample.swiperefresh

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SwipeRefreshViewModel: ViewModel() {

    val showPullRefresh: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun mockReq() {
        viewModelScope.launch {
            showPullRefresh.postValue(true)
            //mock delay
            delay(2000)
            showPullRefresh.postValue(false)
        }
    }
}