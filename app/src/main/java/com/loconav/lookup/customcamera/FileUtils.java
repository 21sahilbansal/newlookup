package com.loconav.lookup.customcamera;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static File getImagefile(Context context) throws IOException {
        File storageDir;
        String imageFileName = "JPEG_" + "Loconav" + "_";
        storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
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
