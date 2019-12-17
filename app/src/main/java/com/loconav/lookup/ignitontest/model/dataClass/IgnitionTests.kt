package com.loconav.lookup.ignitontest.model.dataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

 class IgnitionTests : Serializable {
    @SerializedName("TEST_1")
    @Expose
    var test1: TestNo? = null
    @SerializedName("TEST_2")
    @Expose
    var test2: TestNo? = null
    @SerializedName("TEST_3")
    @Expose
    var test3: TestNo? = null

    fun getTestAtPosition( position : Int) : TestNo?{
        when(position){
            1 -> return test1
            2 -> return test2
            3 -> return test3
            else -> return null
        }
    }
}