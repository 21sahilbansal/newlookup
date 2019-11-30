package com.loconav.lookup.ignitontest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loconav.lookup.ignitontest.model.repository.IgnitionTestApiService
import com.loconav.lookup.utils.DataWrapper

class IgnitionTestViewModel : ViewModel() {
    private val ignitionTestApiService: IgnitionTestApiService? = IgnitionTestApiService()


    fun getIgnitionTestData(deviceId: String, testStartTime: String): MutableLiveData<DataWrapper<IgnitionTestData>>? {
        return ignitionTestApiService?.let { it.getIgnitionData(deviceId, testStartTime) }
    }

}


