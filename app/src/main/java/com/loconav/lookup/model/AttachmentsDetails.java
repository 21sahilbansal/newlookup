package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AttachmentsDetails implements Parcelable
    {
        @SerializedName("id")
         String id;
        @SerializedName("updated_at")
         String updatedAt;
        @SerializedName("urls")
         Urls urls;
        @SerializedName("tag")
        String tag;

        protected AttachmentsDetails(Parcel in) {
            id = in.readString();
            updatedAt = in.readString();
            urls = in.readParcelable(Urls.class.getClassLoader());
            tag = in.readString();
        }

        public static final Creator<AttachmentsDetails> CREATOR = new Creator<AttachmentsDetails>() {
            @Override
            public AttachmentsDetails createFromParcel(Parcel in) {
                return new AttachmentsDetails(in);
            }

            @Override
            public AttachmentsDetails[] newArray(int size) {
                return new AttachmentsDetails[size];
            }
        };

        public String getTag() {
            return tag;
        }
        public void setTag(String tag) {
            this.tag = tag;
        }
        public String getId ()
        {
            return id;
        }
        public void setId (String id)
        {
            this.id = id;
        }
        public String getUpdatedAt()
        {
            return updatedAt;
        }
        public void setUpdatedAt(String updatedAt)
        {
            this.updatedAt = updatedAt;
        }
        public Urls getUrls ()
        {
            return urls;
        }
        public void setUrls (Urls urls)
        {
            this.urls = urls;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(updatedAt);
            dest.writeParcelable(urls, flags);
            dest.writeString(tag);
        }
    }
