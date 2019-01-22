package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sejal on 26-07-2018.
 */

public class RepairRequirements implements Parcelable {

    @SerializedName("device_serial_number")
    @Expose
    private String device_serial_number;

    @SerializedName("reason_id")
    @Expose
    private int reason_id;

    @SerializedName("remarks")
    @Expose
    private String remarks;

    @SerializedName("repair_data")
    @Expose
    private String repairData;

    @SerializedName("pre_repair_images")
    @Expose
    private ArrayList<Attachments> pre_repair_images;

    @SerializedName("post_repair_images")
    @Expose
    private ArrayList<Attachments> post_repair_images;

    public RepairRequirements(){}


    private RepairRequirements(Parcel in) {
        device_serial_number = in.readString();
        reason_id = in.readInt();
        remarks = in.readString();
        repairData = in.readString();
        pre_repair_images = in.createTypedArrayList(Attachments.CREATOR);
        post_repair_images = in.createTypedArrayList(Attachments.CREATOR);
    }

    public static final Creator<RepairRequirements> CREATOR = new Creator<RepairRequirements>() {
        @Override
        public RepairRequirements createFromParcel(Parcel in) {
            return new RepairRequirements(in);
        }

        @Override
        public RepairRequirements[] newArray(int size) {
            return new RepairRequirements[size];
        }
    };

    public String getDevice_id() {
        return device_serial_number;
    }

    public void setDevice_id(String device_id) {
        this.device_serial_number = device_id;
    }

    public int getReason_id() {
        return reason_id;
    }

    public void setReason_id(int reason_id) {
        this.reason_id = reason_id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRepairData() {
        return repairData;
    }

    public void setRepairData(String repairData) {
        this.repairData = repairData;
    }

    public ArrayList<Attachments> getPre_repair_images() {
        return pre_repair_images;
    }

    public void setPre_repair_images(ArrayList<Attachments> pre_repair_images) {
        this.pre_repair_images = pre_repair_images;
    }

    public ArrayList<Attachments> getPost_repair_images() {
        return post_repair_images;
    }

    public void setPost_repair_images(ArrayList<Attachments> post_repair_images) {
        this.post_repair_images = post_repair_images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(device_serial_number);
        dest.writeInt(reason_id);
        dest.writeString(remarks);
        dest.writeString(repairData);
        dest.writeTypedList(pre_repair_images);
        dest.writeTypedList(post_repair_images);
    }
}
