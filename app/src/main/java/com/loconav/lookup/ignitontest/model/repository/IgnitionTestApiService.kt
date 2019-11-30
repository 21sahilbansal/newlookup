package com.loconav.lookup.ignitontest.model.repository

import androidx.lifecycle.MutableLiveData
import com.loconav.lookup.ignitontest.model.dataClass.IgnitionTestData
import com.loconav.lookup.network.RetrofitCallback
import com.loconav.lookup.network.rest.ApiClient
import com.loconav.lookup.network.rest.ApiInterface
import com.loconav.lookup.utils.DataWrapper
import retrofit2.Call
import retrofit2.Response

class IgnitionTestApiService {
    private val apiService = ApiClient.getClient()?.create(ApiInterface::class.java)

    var mutableLiveData: MutableLiveData<DataWrapper<IgnitionTestData>> = MutableLiveData()

    var dataWrapper: DataWrapper<IgnitionTestData> = DataWrapper()

    fun getIgnitionData(deviceId: String, testStartTime: String): MutableLiveData<DataWrapper<IgnitionTestData>> {
        apiService?.getIgnitionTestData(deviceId, testStartTime)?.enqueue(object : RetrofitCallback<IgnitionTestData>() {
            override fun handleSuccess(call: Call<IgnitionTestData>?, response: Response<IgnitionTestData>?) {
                var ignitionResponse = response?.body()
                dataWrapper.data = ignitionResponse
                mutableLiveData.postValue(dataWrapper)
            }

            override fun handleFailure(call: Call<IgnitionTestData>?, t: Throwable?) {
                dataWrapper.throwable = t
                mutableLiveData.postValue(dataWrapper)
            }
        })
        return mutableLiveData
    }
}
