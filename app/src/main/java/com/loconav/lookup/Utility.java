package com.loconav.lookup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import id.zelory.compressor.Compressor;

import static com.loconav.lookup.EncodingDecoding.encodeToBase64;

/**
 * Created by prateek on 15/02/18.
 */

public class Utility {

    public static boolean isStringEmptyOrNull(String checkString) {
        if(checkString == null || checkString.trim().equals("")) {
            return true;
        }else
            return false;
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Boolean matchregex(String str,String str2) {
//        Pattern pattern = Pattern.compile(str);
//        Matcher matcher = pattern.matcher(str2);
//        Boolean ab= matcher.find();
        return str.matches(str2);
    }


    public static String reduceBititmap(Bitmap bitmap, Context context) throws Exception
    {
        File f=new FileUtility().getImagefile(context);
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
//    private static String formatDate(long milliseconds) /* This is your topStory.getTime()*1000 */ {
//        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss");
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(milliseconds);
//        TimeZone tz = TimeZone.getDefault();
//        sdf.setTimeZone(tz);
//        return sdf.format(calendar.getTime());
//    }




}
