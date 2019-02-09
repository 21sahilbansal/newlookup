package com.loconav.lookup.customcamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.loconav.lookup.application.LookUpApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.loconav.lookup.Constants.FILE_PROVIDER_AUTHORITY;
import static com.loconav.lookup.EncodingDecoding.encodeToBase64;

public class ImageUtils {

    private static ImageUri compressImageFile(ImageUri imageUri, Context context) throws IOException {
        File imagefile=getImagefile();
        FileOutputStream fout=new FileOutputStream(imagefile);
        Bitmap bitmap= MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri.getUri());
        bitmap= Bitmap.createScaledBitmap(bitmap,(bitmap.getWidth()*30)/100,(bitmap.getHeight()*30)/100,true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fout);
        ImageUri compressedImageUri=new ImageUri();
        compressedImageUri.setUri(FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, imagefile));
        return compressedImageUri;
    }

    public static ArrayList<ImageUri> compressImageList(ArrayList<ImageUri> imageUriArrayList, Context context) throws IOException {
        ArrayList<ImageUri> newImageUriList=new ArrayList<>();
        for(ImageUri imageUri :imageUriArrayList)
        {
            newImageUriList.add(compressImageFile(imageUri,context));
        }
        return newImageUriList;
    }

    public static File getImagefile() throws IOException {
        File storageDir;
        String imageFileName = "JPEG_" + "Loconav" + "_";
        storageDir = LookUpApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
    }

    public static Bitmap getThumbnailImage(Uri image, Context context) throws IOException
    {
        final int THUMBSIZE = 256;//pixels
        Log.e("the image is ","the big image path is "+image.getPath());
        return  ThumbnailUtils.extractThumbnail(BitmapFactory.decodeStream(context.getContentResolver().openInputStream(image)),
                THUMBSIZE, THUMBSIZE);
    }

    public static String getbase64Image(Bitmap bitmap)
    {
        String str= "data:image/png;base64,"+encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG,100);
        return str;
    }


}
