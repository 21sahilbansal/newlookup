package com.loconav.lookup.newfastag.model

import com.loconav.lookup.Toaster
import com.loconav.lookup.model.FastTagResponse
import com.loconav.lookup.network.RetrofitCallback
import com.loconav.lookup.network.rest.ApiClient
import com.loconav.lookup.network.rest.ApiInterface
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
                EventBus.getDefault().post(NewFastagEvent(NewFastagEvent.TRUCK_ID_NOT_VERIFIED,t?.message))
            }
        })
    }

    fun validateScannedFastag(vehicleID: Int, fastagno: String) {
        apiService.verifyScannedFastag(vehicleID, fastagno).enqueue(object : RetrofitCallback<ResponseBody>() {
            override fun handleSuccess(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                EventBus.getDefault().post(NewFastagEvent(NewFastagEvent.SCANNED_CORRECT_FASTAG))
            }

            override fun handleFailure(call: Call<ResponseBody>?, t: Throwable?) {
                EventBus.getDefault().post(NewFastagEvent(NewFastagEvent.SCANNED_WRONG_FASTAG,t!!.message) )
            }
        })}

        fun getdataAfterFastagcreation(truckNo: String) {
            apiService.validateTruckNumberOrFastagNumber(truckNo).enqueue(object : RetrofitCallback<FastTagResponse>() {
                override fun handleSuccess(call: Call<FastTagResponse>, response: Response<FastTagResponse>) {
                 EventBus.getDefault().post(NewFastagEvent(NewFastagEvent.GOT_DATA_FOR_FASTAG_PHOTOS,response.body()))
                }

                override fun handleFailure(call: Call<FastTagResponse>, t: Throwable) {
                    EventBus.getDefault().post(NewFastagEvent(NewFastagEvent.DATA_FOR_FASTAG_NOT_FOUND,t!!.message) )

                }
            })
        }
    }


