package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sejal on 30-07-2018.
 */

public class Input implements Parcelable {

    private String name;
    private String key;
    private String field_type;
    private String hint;
    private ArrayList<String> data;

    public Input(String name, String key, String field_type, String hint) {
        this.name = name;
        this.key = key;
        this.hint=hint;
        this.field_type = field_type;
    }

    protected Input(Parcel in) {
        name = in.readString();
        key = in.readString();
        field_type = in.readString();
        hint = in.readString();
        data = in.createStringArrayList();
    }

    public static final Creator<Input> CREATOR = new Creator<Input>() {
        @Override
        public Input createFromParcel(Parcel in) {
            return new Input(in);
        }

        @Override
        public Input[] newArray(int size) {
            return new Input[size];
        }
    };

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getField_type() {
        return field_type;
    }

    public void setField_type(String field_type) {
        this.field_type = field_type;
    }

    public  ArrayList<String> getData() {
        return data;
    }

    public void setData( ArrayList<String> data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(key);
        dest.writeString(field_type);
        dest.writeString(hint);
        dest.writeStringList(data);
    }
}
