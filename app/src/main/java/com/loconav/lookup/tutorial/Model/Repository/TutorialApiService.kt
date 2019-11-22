package com.loconav.lookup.tutorial.Model.Repository

import androidx.lifecycle.MutableLiveData
import com.loconav.lookup.network.rest.ApiClient
import com.loconav.lookup.network.rest.ApiInterface

class TutorialApiService {
    private val apiService = ApiClient.getClient()?.create(ApiInterface::class.java)

    var mutableLiveData: MutableLiveData<DataWrapper<Tuori>> = MutableLiveData<DataWrapper<IgnitionTest>>()

    var dataWrapper: DataWrapper<IgnitionTest> = DataWrapper<IgnitionTest>()

    fun readIgnitionData(deviceId: String, time: String): MutableLiveData<DataWrapper<IgnitionTest>> {
        apiService?.getIgnitionTestData(deviceId,time)?.enqueue(object : RetrofitCallback<IgnitionTest>() {
            override fun handleSuccess(call: Call<IgnitionTest>?, response: Response<IgnitionTest>?) {
                var igResponse = response?.body()
                dataWrapper.data = igResponse
                mutableLiveData.postValue(dataWrapper)
            }

            override fun handleFailure(call: Call<IgnitionTest>?, t: Throwable?) {
                dataWrapper.throwable = t
                mutableLiveData.postValue(dataWrapper)
            }
        })

        return mutableLiveData
}