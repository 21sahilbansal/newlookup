package com.loconav.lookup.ignitontest.model.dataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class IgnitionTestData : Serializable {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null
    @SerializedName("no_of_test_headers")
    @Expose
    var noOfTests: Int? = null
    @SerializedName("api_result")
    @Expose
    var apiResult: ApiResult? = null
    @SerializedName("restart_test")
    @Expose
    var restartTest: Boolean? = null
    @SerializedName("time_to_call_api")
    @Expose
    var timeToCallApi: Int? = null
    @SerializedName("next_test")
    @Expose
    var nextTest: String? = null
    @SerializedName("ignition_tests")
    @Expose
    var ignitionTests: IgnitionTests? = null


}