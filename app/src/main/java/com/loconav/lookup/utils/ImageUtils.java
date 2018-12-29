package com.loconav.lookup.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

import id.zelory.compressor.Compressor;

import static com.loconav.lookup.EncodingDecoding.encodeToBase64;

public class ImageUtils {
    public static String reduceBititmap(Bitmap bitmap, Context context) throws Exception
    {
        File f=new FileUtils().getImagefile(context);
        FileOutputStream fout=new FileOutputStream(f);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
        Bitmap compressedImageBitmap = new Compressor(context).setQuality(70).compressToBitmap(f);
        int height=compressedImageBitmap.getHeight();
        int width=compressedImageBitmap.getWidth();
        compressedImageBitmap=Bitmap.createScaledBitmap(compressedImageBitmap,(width*90)/100,(height*90)/100,true);
        String str= "data:image/png;base64,"+encodeToBase64(compressedImageBitmap, Bitmap.CompressFormat.JPEG,50);
        Log.e("SIZE OF",""+str.length());
        return str;
    }
}
