package com.loconav.lookup;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class FileUtility {
    public File getfile(Context context) throws IOException {
        File storageDir;
        String imageFileName = "JPEG_" + "sourav" + "_";
        storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }
}
