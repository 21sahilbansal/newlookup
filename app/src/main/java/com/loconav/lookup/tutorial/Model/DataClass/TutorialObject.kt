package com.loconav.lookup.tutorial.Model.DataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TutorialObject : Serializable {
    @SerializedName("id")
    @Expose
    private var id : Int ?= null
    @SerializedName("title")
    @Expose
    private var title : String ?=null
    @SerializedName("description")
    @Expose
    private var description : String ?= null
    @SerializedName("url")
    @Expose
    private var url : String ?= null


}