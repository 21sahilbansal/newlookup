package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by prateek on 12/05/18.
 */

public class LookupResponse implements Parcelable {

    @SerializedName("data")
    private List<Entity> data ;
    @SerializedName("passed")
    private Boolean passed;

    public LookupResponse()
    {

    }

    protected LookupResponse(Parcel in) {
        data = in.createTypedArrayList(Entity.CREATOR);
        byte tmpPassed = in.readByte();
        passed = tmpPassed == 0 ? null : tmpPassed == 1;
    }

    public static final Creator<LookupResponse> CREATOR = new Creator<LookupResponse>() {
        @Override
        public LookupResponse createFromParcel(Parcel in) {
            return new LookupResponse(in);
        }

        @Override
        public LookupResponse[] newArray(int size) {
            return new LookupResponse[size];
        }
    };

    public List<Entity> getData() {
        return data;
    }

    public void setData(List<Entity> data) {
        this.data = data;
    }

    public Boolean getPassed() { return passed; }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
        dest.writeByte((byte) (passed == null ? 0 : passed ? 1 : 2));
    }
}
