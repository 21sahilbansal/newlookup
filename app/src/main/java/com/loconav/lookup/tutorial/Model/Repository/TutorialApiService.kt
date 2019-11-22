package com.loconav.lookup.tutorial.Model.Repository

import androidx.lifecycle.MutableLiveData
import com.loconav.lookup.network.RetrofitCallback
import com.loconav.lookup.network.rest.ApiClient
import com.loconav.lookup.network.rest.ApiInterface
import com.loconav.lookup.tutorial.Model.DataClass.TutorialData
import com.loconav.lookup.utils.DataWrapper
import retrofit2.Call
import retrofit2.Response

class TutorialApiService {
    private val apiService = ApiClient.getClient()?.create(ApiInterface::class.java)

    var mutableLiveData: MutableLiveData<DataWrapper<TutorialData>> = MutableLiveData<DataWrapper<TutorialData>>()

    var dataWrapper: DataWrapper<TutorialData> = DataWrapper<TutorialData>()

    fun getTutorialData(): MutableLiveData<DataWrapper<TutorialData>> {
        apiService?.learningTutorials?.enqueue(object : RetrofitCallback<TutorialData>() {
            override fun handleSuccess(call: Call<TutorialData>?, response: Response<TutorialData>?) {
                var igResponse = response?.body()
                dataWrapper.data = igResponse
                mutableLiveData.postValue(dataWrapper)
            }

            override fun handleFailure(call: Call<TutorialData>?, t: Throwable?) {
                dataWrapper.throwable = t
                mutableLiveData.postValue(dataWrapper)
            }
        })

        return mutableLiveData
    }
}