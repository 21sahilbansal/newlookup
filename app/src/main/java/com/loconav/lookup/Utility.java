package com.loconav.lookup;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

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
}
