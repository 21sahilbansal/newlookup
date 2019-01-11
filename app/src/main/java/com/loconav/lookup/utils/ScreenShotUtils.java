package com.loconav.lookup.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.loconav.lookup.customcamera.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ScreenShotUtils {
    public static Bitmap takescreenshot(View v) {
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;
    }

    public static Bitmap takescreenshotOfRootView(View v) {
        return takescreenshot(v.getRootView());
    }

    public static void storeScreenshot(Bitmap bitmap, Context context)
    {
        File imageFile= null;
        try {
            imageFile = FileUtils.getImagefile(context);
        } catch (IOException e) {
            Log.e("exception image","There is an exception in image");
        }
        OutputStream out = null;

        try {
            out = new FileOutputStream(imageFile);
            // choose JPEG format
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
        } catch (FileNotFoundException e) {
            Log.e("the exception ","there is an exception in screenshot");
            // manage exception ...
        } catch (IOException e) {
            Log.e("the exception ","there is an exception in screenshot");
            // manage exception ...
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception exc) {
            }
        }
    }
}
