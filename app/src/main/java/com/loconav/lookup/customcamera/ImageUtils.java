package com.loconav.lookup.customcamera;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.loconav.lookup.application.LookUpApplication;
import com.loconav.lookup.utils.TimeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.ArrayList;

import static com.loconav.lookup.Constants.FILE_PROVIDER_AUTHORITY;
import static com.loconav.lookup.EncodingDecoding.encodeToBase64;

public class ImageUtils {

    private static final Context FILE_CONTEXT = LookUpApplication.getInstance();

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
        Bitmap bitmapWithTimeStamp = getImageWithTimeStamp(bitmap,imageUri);
        String str = "data:image/png;base64," + encodeToBase64(bitmapWithTimeStamp, Bitmap.CompressFormat.JPEG, 100);
        return str;
    }


    private static Bitmap getImageWithTimeStamp(Bitmap bitmap, ImageUri imageUri) {
        Long imageTakenEpochTime = imageUri.getImageEpochTime();
        Boolean afterTimeComaprison = compareTime(imageTakenEpochTime);
        Boolean noDateAvailable = false;
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        if (afterTimeComaprison) {
            paint.setColor(Color.GREEN);
        } else {
            if(imageTakenEpochTime == 0000000000000){
                noDateAvailable = true;
                paint.setColor(Color.YELLOW);

            }
            paint.setColor(Color.RED);
        }
        String imageTakenDate;
        Time imageTakenHours;
        paint.setTextSize(25);
        paint.setTextAlign(Paint.Align.RIGHT);
        if(noDateAvailable){
          imageTakenDate =  TimeUtils.getDate(String.valueOf(System.currentTimeMillis()));
          imageTakenHours = new Time(System.currentTimeMillis());
        }else {
        imageTakenDate = TimeUtils.getDate((String.valueOf(imageTakenEpochTime)));
        imageTakenHours = new Time(imageTakenEpochTime);}
        String imageDateTime = imageTakenDate + " " + imageTakenHours;
        canvas.drawText(imageDateTime, canvas.getWidth()-10, canvas.getHeight()-10, paint);
        return mutableBitmap;
    }


    private static boolean compareTime(long imageTakentime) {
        Long currentSystemTime = System.currentTimeMillis();
        Long difference = currentSystemTime - imageTakentime;
        if (difference < (60 * 30000)) {
            return true;

        } else {
            return false;
        }

    }


    public static String getEpochTimeOfGalleryImage(Uri uri) {
        String zeroepochtime = "0000000000000";
        Cursor cursor =FILE_CONTEXT.getContentResolver().query(uri, null, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow("last_modified");
        cursor.moveToFirst();
        String date = cursor.getString(column_index);
        if(date == null){
            return  zeroepochtime;
        }
        else {
            return date;
        }
    }

    public static String getDateOfCameraTakenPhoto(Uri uri){

        String zeroepochtime = "0000000000000";
        try {
            InputStream inputStream = FILE_CONTEXT.getContentResolver().openInputStream(uri);
            try {
                Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
                for (Directory directory1 : metadata.getDirectories()) {
                    if (directory1.getName().equals("Exif IFD0")) {
                        for (Tag tag : directory1.getTags()) {
                            if (tag.getTagName().equals("Date/Time")) {
                                return tag.getDescription();
                            }
                        }
                    }
                }
            } catch (ImageProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return zeroepochtime;
    }




}



