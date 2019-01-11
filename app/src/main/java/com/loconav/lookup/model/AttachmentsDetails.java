package com.loconav.lookup.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AttachmentsDetails implements Serializable
    {
        @SerializedName("id")
         String id;
        @SerializedName("updated_at")
         String updatedAt;
        @SerializedName("urls")
         Urls urls;
        @SerializedName("tag")
        String tag;

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
    }
