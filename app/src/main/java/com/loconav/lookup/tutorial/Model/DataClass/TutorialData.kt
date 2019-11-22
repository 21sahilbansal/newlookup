package com.loconav.lookup.tutorial.Model.DataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TutorialData : Serializable {
    @SerializedName("tutorial_list")
    @Expose
    private var tutorial_list : ArrayList<TutorialObject> ?= null
}