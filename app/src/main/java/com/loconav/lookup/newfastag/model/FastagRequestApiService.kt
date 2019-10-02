package com.loconav.lookup.newfastag.model

import com.loconav.lookup.Toaster
import com.loconav.lookup.base.PubSubEvent
import com.loconav.lookup.network.RetrofitCallback
import com.loconav.lookup.network.rest.ApiClient
import com.loconav.lookup.network.rest.ApiInterface
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

 class FastagRequestApiService  {

        private val apiService = ApiClient.getClient().create(ApiInterface::class.java)

       fun validateTruckNo(truckNo: String) {
            apiService.getVehcileFastagDetails(truckNo).enqueue(object : RetrofitCallback<VehicleDetails>(){
                override fun handleSuccess(call: Call<VehicleDetails>?, response: Response<VehicleDetails>?) {
                    EventBus.getDefault().post(NewFastagEvent(NewFastagEvent.Truck_ID_VERIFIED,response?.body()))
                }

                override fun handleFailure(call: Call<VehicleDetails>?, t: Throwable?) {
                      Toaster.makeToast(t?.message)
                }
            })
        }
    }


