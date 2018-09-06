package com.loconav.lookup;

import android.content.Intent;

import com.loconav.lookup.base.PubSubEvent;

/**
 * Created by sejal on 19-07-2018.
 */

public class GalleryEvents extends PubSubEvent{

    public GalleryEvents(String message, Object object) {
        super(message, object);
    }

    public GalleryEvents(String message) {
        super(message);
    }

    public static  final  String IMAGE_COMPRESSED = "image_compressed";

}
