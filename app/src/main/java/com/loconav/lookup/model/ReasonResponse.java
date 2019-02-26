package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sejal on 25-07-2018.
 */

public class ReasonResponse implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("slug")
    @Expose
    private String slug;

    @SerializedName("reasons")
    @Expose
    private List<ReasonTypeResponse> reasons;

    @SerializedName("additional_fields")
    @Expose
    private ArrayList<Input> additional_fields;

    @SerializedName("icon_url")
    private String iconUrl;


    public ReasonResponse(Parcel in) {
        id = in.readInt();
        name = in.readString();
        slug = in.readString();
        reasons = in.createTypedArrayList(ReasonTypeResponse.CREATOR);
        additional_fields = in.createTypedArrayList(Input.CREATOR);
        iconUrl = in.readString();
        color = in.readInt();
    }

    public ReasonResponse(int id, String name, List<ReasonTypeResponse> reasons,
                          ArrayList<Input> additional_fields, String iconUrl,String slug) {
        this.id = id;
        this.name = name;
        this.additional_fields=additional_fields;
        this.reasons = reasons;
        this.iconUrl = iconUrl;
        this.slug=slug;
    }

    public static final Creator<ReasonResponse> CREATOR = new Creator<ReasonResponse>() {
        @Override
        public ReasonResponse createFromParcel(Parcel in) {
            return new ReasonResponse(in);
        }

        @Override
        public ReasonResponse[] newArray(int size) {
            return new ReasonResponse[size];
        }
    };

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;

    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ArrayList<Input> getAdditional_fields() {
        return additional_fields;
    }

    public void setAdditional_fields(ArrayList<Input> additional_fields) {
        this.additional_fields = additional_fields;
    }

    private int color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ReasonTypeResponse> getReasons() {
        return reasons;
    }

    public void setReasons(List<ReasonTypeResponse> reasons) {
        this.reasons = reasons;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(slug);
        dest.writeTypedList(reasons);
        dest.writeTypedList(additional_fields);
        dest.writeString(iconUrl);
        dest.writeInt(color);
    }


}
