package com.loconav.lookup.model;

import com.google.gson.annotations.SerializedName;

public class AttachmentsDetails
    {
        @SerializedName("id")
         String id;
        @SerializedName("updated_at")
         String updated_at;
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
        public String getUpdated_at ()
        {
            return updated_at;
        }
        public void setUpdated_at (String updated_at)
        {
            this.updated_at = updated_at;
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
