package com.loconav.lookup.ignitontest.model.repository

import androidx.lifecycle.MutableLiveData
import com.loconav.lookup.ignitontest.model.dataClass.IgnitionTestData
import com.loconav.lookup.network.rest.ApiClient
import com.loconav.lookup.network.rest.ApiInterface
import com.loconav.lookup.utils.DataWrapper

class IgnitionTestApiService {
    private val apiService = ApiClient.getClient()?.create(ApiInterface::class.java)

    var mutableLiveData: MutableLiveData<DataWrapper<IgnitionTestData>> = MutableLiveData<DataWrapper<List<TutorialObject>>>()

    var dataWrapper: DataWrapper<List<>> = DataWrapper()

    fun getTutorialData(): MutableLiveData<DataWrapper<List<TutorialObject>>> {
        apiService?.learningTutorials?.enqueue(object : RetrofitCallback<List<TutorialObject>>() {
            override fun handleSuccess(call: Call<List<TutorialObject>>?, response: Response<List<TutorialObject>>?) {
                var tutorialResponse = response?.body()
                dataWrapper.data = tutorialResponse
                mutableLiveData.postValue(dataWrapper)
            }
            override fun handleFailure(call: Call<List<TutorialObject>>?, t: Throwable?) {
                dataWrapper.throwable = t
                mutableLiveData.postValue(dataWrapper)
            }
        })
        return mutableLiveData
    }
}
}