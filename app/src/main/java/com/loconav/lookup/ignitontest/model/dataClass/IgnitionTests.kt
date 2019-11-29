package com.loconav.lookup.ignitontest.model.dataClass

import com.google.gson.annotations.SerializedName
import java.util.*

class IgnitionTests {

    @SerializedName("TEST_1")
    var TEST_1: TEST1? = null

    @SerializedName("TEST_2")
    var TEST_2: TEST2? = null

    @SerializedName("TEST_3")
    var TEST_3: TEST3? = null


    fun getTestAtPosition(testPosition: Int):   Test? {
        when (testPosition) {
            1 -> {
                return TEST_1 as Test
            }
            2 -> {
                return TEST_2 as Test
            }
            3 -> {
                return TEST_3 as Test
            }
            else -> return null
        }

    }

}