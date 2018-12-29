package com.loconav.lookup.customcamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static File getImagefile(Context context) throws IOException {
        File storageDir;
        String imageFileName = "JPEG_" + "Loconav" + "_";
        storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }
    public static Bitmap bitmapTouri(Context context, Uri imageUri) {
        Bitmap bm = null;
        try {
            bm = (MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }
    public static void deleteFiles(Context context)
    {
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (dir.isDirectory())
        {
            for (File f:dir.listFiles())
            {
                f.delete();
            }
        }
    }
}
