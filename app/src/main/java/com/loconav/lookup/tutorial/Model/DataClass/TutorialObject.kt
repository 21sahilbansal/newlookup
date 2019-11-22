package com.loconav.lookup.tutorial.Model.DataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TutorialObject : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
}