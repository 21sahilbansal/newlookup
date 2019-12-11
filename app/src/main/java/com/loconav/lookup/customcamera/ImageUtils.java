package com.loconav.lookup.customcamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.crashlytics.android.Crashlytics;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.loconav.lookup.application.LookUpApplication;
import com.loconav.lookup.utils.TimeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.ArrayList;

import static com.loconav.lookup.Constants.FILE_PROVIDER_AUTHORITY;
import static com.loconav.lookup.EncodingDecoding.encodeToBase64;

public class ImageUtils {

    private static final Context FILE_CONTEXT = LookUpApplication.getInstance();
    private static final String imageFormat = "data:image/png;base64,";

    private static ImageUri compressImageFile(ImageUri imageUri) throws IOException {
        File imagefile = getImagefile();
        FileOutputStream fout = new FileOutputStream(imagefile);
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(FILE_CONTEXT.getContentResolver(), imageUri.getUri());
        bitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.getWidth() * 30) / 100, (bitmap.getHeight() * 30) / 100, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fout);
        //    ImageUri compressedImageUri = new ImageUri();
        imageUri.setUri(FileProvider.getUriForFile(FILE_CONTEXT, FILE_PROVIDER_AUTHORITY, imagefile));
        return imageUri;
    }

    public static ArrayList<ImageUri> compressImageList(ArrayList<ImageUri> imageUriArrayList) throws IOException {
        ArrayList<ImageUri> newImageUriList = new ArrayList<>();
        for (ImageUri imageUri : imageUriArrayList) {
            newImageUriList.add(compressImageFile(imageUri));
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

    public static Bitmap getThumbnailImage(Uri image, Context context) throws IOException {
        final int THUMBSIZE = 256;//pixels
        Log.e("the image is ", "the big image path is " + image.getPath());
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeStream(context.getContentResolver().openInputStream(image)),
                THUMBSIZE, THUMBSIZE);
    }


    public static String getbase64Image(Bitmap bitmap, ImageUri imageUri) {
        Bitmap bitmapWithTimeStamp = getImageWithTimeStamp(bitmap, imageUri);
        String str = imageFormat + encodeToBase64(bitmapWithTimeStamp, Bitmap.CompressFormat.JPEG, 100);
        return str;
    }


    private static Bitmap getImageWithTimeStamp(Bitmap bitmap, ImageUri imageUri) {
        Long imageTakenEpochTime = imageUri.getImageEpochTime();
        boolean timeOfImage = compareTime(imageTakenEpochTime);
        boolean noDateAvailable = false;
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(1.5F);
        if (timeOfImage) {
            paint.setColor(Color.BLUE);
        } else {
            if (imageTakenEpochTime == 0000000000000) {
                noDateAvailable = true;
                paint.setColor(Color.YELLOW);
            } else {
                paint.setColor(Color.RED);
            }
        }
        String imageTakenDate;
        Time imageTakenHours;
        paint.setTextSize(28);
        paint.setTextAlign(Paint.Align.RIGHT);
        if (noDateAvailable) {
            imageTakenDate = TimeUtils.getDate(String.valueOf(System.currentTimeMillis()));
            imageTakenHours = new Time(System.currentTimeMillis());
        } else {
            imageTakenDate = TimeUtils.getDate((String.valueOf(imageTakenEpochTime)));
            imageTakenHours = new Time(imageTakenEpochTime);
        }
        String imageDateTime = String.format("%s, %s", imageTakenDate, imageTakenHours);
        canvas.drawText(imageDateTime, canvas.getWidth() - 10, canvas.getHeight() - 10, paint);
        return mutableBitmap;
    }


    private static boolean compareTime(long imageTakentime) {
        Long currentSystemTime = System.currentTimeMillis();
        Long difference = currentSystemTime - imageTakentime;
        if (difference < (60 * 30000)) {
            return true;
        } else
            return false;
    }


//    public static String getEpochTimeOfGalleryImage(Uri uri) {
//        String zeroEpochTime = "0000000000000";
//        if(uri == null){
//            return zeroEpochTime;
//        }
//        Cursor cursor = FILE_CONTEXT.getContentResolver().query(uri, null, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow("last_modified");
//        if (cursor.getColumnCount() > 0) {
//            cursor.moveToFirst();
//        } else {
//            return zeroEpochTime;
//        }
//        String date = cursor.getString(column_index);
//        if (date == null) {
//            return zeroEpochTime;
//        } else {
//            return date;
//        }
//    }

    public static String getDateOfCameraTakenPhoto(Uri uri,Boolean camerImage) {
        String zeroepochtime = "0000000000000";
        if(uri == null){
            Crashlytics.logException(new Throwable("image uri is null"));
            return zeroepochtime;
        }
        try {
            InputStream inputStream = FILE_CONTEXT.getContentResolver().openInputStream(uri);
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
            for (Directory directory : metadata.getDirectories()) {
                if (directory.getName().equals("Exif IFD0")) {
                    for (Tag tag : directory.getTags()) {
                        if (tag.getTagName().equals("Date/Time")) {
                            if(tag.getDescription().contains(":")){
                                if(camerImage){
                                    return tag.getDescription();
                                }
                                return String.valueOf(TimeUtils.getEpochTime(tag.getDescription()));
                            }else {
                            return tag.getDescription();//date retu
                            }
                        }

                    }
                }
            }

        } catch (Exception e) {
            Crashlytics.logException(new Throwable(e));
            e.printStackTrace();
        }
        return zeroepochtime;
    }
}



