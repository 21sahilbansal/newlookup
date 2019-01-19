package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Notes implements Parcelable {
    @SerializedName("type_of_goods")
    private String typeOfGoods;
    @SerializedName("device_model")
    private String deviceModel;
    @SerializedName("model")
    private String model;
    @SerializedName("trip_button")
    private String trip_button;
    @SerializedName("immobilizer")
    private String immobilizer;
    @SerializedName("sim_number")
    private String sim_number;
    @SerializedName("owner_name")
    private String owner_name;
    @SerializedName("location")
    private String location;
    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("odometer_reading")
    private String odometer_reading;
    @SerializedName("chassis_number")
    private String chassis_number;
    @SerializedName("dealer_name")
    private String dealer_name;
    @SerializedName("sos")
    private String sos;
    @SerializedName("clientid")
    private String clientid;
    @SerializedName("installdate")
    private String installdate;

    public Notes()
    {

    }

    protected Notes(Parcel in) {
        typeOfGoods = in.readString();
        deviceModel = in.readString();
        model = in.readString();
        trip_button = in.readString();
        immobilizer = in.readString();
        sim_number = in.readString();
        owner_name = in.readString();
        location = in.readString();
        manufacturer = in.readString();
        odometer_reading = in.readString();
        chassis_number = in.readString();
        dealer_name = in.readString();
        sos = in.readString();
        clientid = in.readString();
        installdate = in.readString();
    }

    public static final Creator<Notes> CREATOR = new Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };

    public String getTrip_button() {
        return trip_button;
    }

    public void setTrip_button(String trip_button) {
        this.trip_button = trip_button;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getInstalldate() {
        return installdate;
    }

    public void setInstalldate(String installdate) {
        this.installdate = installdate;
    }

    public String getTypeOfGoods()
    {
        return typeOfGoods;
    }

    public void setTypeOfGoods(String typeOfGoods)
    {
        this.typeOfGoods = typeOfGoods;
    }

    public String getDeviceModel()
    {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel)
    {
        this.deviceModel = deviceModel;
    }

    public String getModel ()
    {
        return model;
    }

    public void setModel (String model)
    {
        this.model = model;
    }

    public String getImmobilizer ()
    {
        return immobilizer;
    }

    public void setImmobilizer (String immobilizer)
    {
        this.immobilizer = immobilizer;
    }

    public String getSim_number ()
    {
        return sim_number;
    }

    public void setSim_number (String sim_number)
    {
        this.sim_number = sim_number;
    }

    public String getOwner_name ()
    {
        return owner_name;
    }

    public void setOwner_name (String owner_name)
    {
        this.owner_name = owner_name;
    }

    public String getLocation ()
    {
        return location;
    }

    public void setLocation (String location)
    {
        this.location = location;
    }

    public String getManufacturer ()
    {
        return manufacturer;
    }

    public void setManufacturer (String manufacturer)
    {
        this.manufacturer = manufacturer;
    }

    public String getOdometer_reading ()
    {
        return odometer_reading;
    }

    public void setOdometer_reading (String odometer_reading)
    {
        this.odometer_reading = odometer_reading;
    }

    public String getChassis_number ()
    {
        return chassis_number;
    }

    public void setChassis_number (String chassis_number)
    {
        this.chassis_number = chassis_number;
    }

    public String getDealer_name ()
    {
        return dealer_name;
    }

    public void setDealer_name (String dealer_name)
    {
        this.dealer_name = dealer_name;
    }

    public String getSos ()
    {
        return sos;
    }

    public void setSos (String sos)
    {
        this.sos = sos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(typeOfGoods);
        dest.writeString(deviceModel);
        dest.writeString(model);
        dest.writeString(trip_button);
        dest.writeString(immobilizer);
        dest.writeString(sim_number);
        dest.writeString(owner_name);
        dest.writeString(location);
        dest.writeString(manufacturer);
        dest.writeString(odometer_reading);
        dest.writeString(chassis_number);
        dest.writeString(dealer_name);
        dest.writeString(sos);
        dest.writeString(clientid);
        dest.writeString(installdate);
    }
}