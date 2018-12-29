package com.loconav.lookup.customcamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import id.zelory.compressor.Compressor;

import static com.loconav.lookup.EncodingDecoding.encodeToBase64;

public class ImageUtils {
    public static String reduceBititmap(Bitmap bitmap, Context context) throws Exception
    {
        File f=new FileUtils().getImagefile(context);
        FileOutputStream fout=new FileOutputStream(f);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
        Bitmap compressedImageBitmap = new Compressor(context).setQuality(100).compressToBitmap(f);
        int height=compressedImageBitmap.getHeight();
        int width=compressedImageBitmap.getWidth();
        compressedImageBitmap=Bitmap.createScaledBitmap(compressedImageBitmap,(width*90)/100,(height*90)/100,true);
        String str= "data:image/png;base64,"+encodeToBase64(compressedImageBitmap, Bitmap.CompressFormat.JPEG,50);
        Log.e("SIZE OF",""+str.length());
        return str;
    }
    public static ImageUri getBitmapFile(Uri image, Context context) throws IOException {
        final int THUMBSIZE = 256;
        Log.e("the image is ","the big image path is "+image.getPath());
        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeStream(context.getContentResolver().openInputStream(image)),
                THUMBSIZE, THUMBSIZE);
        File f=new FileUtils().getImagefile(context);
        FileOutputStream fout=new FileOutputStream(f);
        ThumbImage.compress(Bitmap.CompressFormat.JPEG, 100, fout);
        ImageUri imageUri=new ImageUri();
        imageUri.setUri(FileProvider.getUriForFile(context,
                "com.lookuploconav.lookup",
                f));
        return imageUri;
    }
}
