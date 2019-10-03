package com.loconav.lookup.newfastag.model

import com.loconav.lookup.Toaster
import com.loconav.lookup.model.FastTagResponse
import com.loconav.lookup.network.RetrofitCallback
import com.loconav.lookup.network.rest.ApiClient
import com.loconav.lookup.network.rest.ApiInterface
import io.reactivex.internal.schedulers.NewThreadWorker
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

class FastagRequestApiService {

    private val apiService = ApiClient.getClient().create(ApiInterface::class.java)

    fun validateTruckNo(truckNo: String) {
        apiService.getVehcileFastagDetails(truckNo).enqueue(object : RetrofitCallback<VehicleDetails>() {
            override fun handleSuccess(call: Call<VehicleDetails>?, response: Response<VehicleDetails>?) {
                EventBus.getDefault().post(NewFastagEvent(NewFastagEvent.Truck_ID_VERIFIED, response?.body()))
            }

            override fun handleFailure(call: Call<VehicleDetails>?, t: Throwable?) {
                Toaster.makeToast(t?.message)
            }
        })
    }

    fun validateScannedFastag(vehicleID: Int, fastagno: String) {
        apiService.verifyScannedFastag(vehicleID, fastagno).enqueue(object : RetrofitCallback<ResponseBody>() {
            override fun handleSuccess(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                EventBus.getDefault().post(NewFastagEvent(NewFastagEvent.Scanned_Correct_Fastag))
            }

            override fun handleFailure(call: Call<ResponseBody>?, t: Throwable?) {
                Toaster.makeToast(t?.message)
            }
        })}

        fun getdataAfterFastagcreation(truckNo: String) {
            apiService.validateTruckNumberOrFastagNumber(truckNo).enqueue(object : RetrofitCallback<FastTagResponse>() {
                override fun handleSuccess(call: Call<FastTagResponse>, response: Response<FastTagResponse>) {
                 EventBus.getDefault().post(NewFastagEvent(NewFastagEvent.Got_data_for_fastag_photos,response))
                }

                override fun handleFailure(call: Call<FastTagResponse>, t: Throwable) {
                    Toaster.makeToast(t.message)
                }
            })
        }
    }



