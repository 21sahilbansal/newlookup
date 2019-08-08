package com.loconav.lookup.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class FastTagResponse {
        @SerializedName("id")
        @Expose
        private Integer id;

        @SerializedName("truck_number")
        @Expose
        private String truckNumber;

        @SerializedName("fastag_serial_number")
        @Expose
        private String fastagSerialNumber;

        @SerializedName("registered_on")
        @Expose
        private Integer registeredOn;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTruckNumber() {
            return truckNumber;
        }

        public void setTruckNumber(String truckNumber) {
            this.truckNumber = truckNumber;
        }

        public String getFastagSerialNumber() {
            return fastagSerialNumber;
        }

        public void setFastagSerialNumber(String fastagSerialNumber) {
            this.fastagSerialNumber = fastagSerialNumber;
        }

        public Integer getRegisteredOn() {
            return registeredOn;
        }

        public void setRegisteredOn(Integer registeredOn) {
            this.registeredOn = registeredOn;
        }

    }