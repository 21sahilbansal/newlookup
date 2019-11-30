package com.loconav.lookup.ignitontest.model.dataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TestNo : Serializable {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("key")
    @Expose
    var key: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("battery")
    @Expose
    var battery: String? = null
    @SerializedName("ignition")
    @Expose
    var ignition: String? = null

}