package com.loconav.lookup.tutorial.Model.DataClass

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TutorialData() :Parcelable {
    @SerializedName("tutorial_list")
    @Expose
    var tutorial_list : ArrayList<TutorialObject> ?= null

    constructor(parcel: Parcel) : this() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TutorialData> {
        override fun createFromParcel(parcel: Parcel): TutorialData {
            return TutorialData(parcel)
        }

        override fun newArray(size: Int): Array<TutorialData?> {
            return arrayOfNulls(size)
        }
    }
}