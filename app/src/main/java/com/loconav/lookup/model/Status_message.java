package com.loconav.lookup.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Status_message implements Serializable {
    @SerializedName("received_at")
    private String received_at;

    @SerializedName("io_state")
    private String io_state;

    public String getReceived_at ()
    {
        return received_at;
    }

    public void setReceived_at (String received_at)
    {
        this.received_at = received_at;
    }

    public String getIo_state ()
    {
        return io_state;
    }

    public void setIo_state (String io_state)
    {
        this.io_state = io_state;
    }


}
