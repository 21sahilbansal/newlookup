package com.loconav.lookup.newfastag.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class VehicleDetails : Serializable {

    @SerializedName("vehicle_id")
    var vehicleId :Int?= null

    @SerializedName("axle_description")
    var axleDescription : String?= null

    @SerializedName("color")
     var color : String ?= null

    @SerializedName("color_hex")
     var colorHex : String ?= null

    @SerializedName("created_at")
     var createdAt : Long?= null
}
