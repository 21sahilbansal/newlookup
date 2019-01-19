package com.loconav.lookup.customcamera;

import com.loconav.lookup.base.PubSubEvent;

/**
 * Created by sejal on 19-07-2018.
 */

class ImagePickerEvent extends PubSubEvent{

    public static final String IMAGE_SELECTED_FROM_GALLERY = "image_selected_from_gallery";
    public static final String IMAGE_SELECTED_FROM_CAMERA = "image_selected_from_camera";

    public ImagePickerEvent(String message, Object object) {
        super(message, object);
    }

    public ImagePickerEvent(String message) {
        super(message);
    }

}
