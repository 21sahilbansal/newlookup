package com.loconav.lookup;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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

    public static Boolean matchregex(String str,String str2){

//        Pattern pattern = Pattern.compile(str);
//        Matcher matcher = pattern.matcher(str2);
//        Boolean ab= matcher.find();
        return str.matches(str2);

    }
}
