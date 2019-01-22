package com.loconav.lookup;

import android.widget.Toast;

import com.loconav.lookup.application.LookUpApplication;

public class Toaster {
    public static void makeToast(String message)
    {
        Toast.makeText(LookUpApplication.getInstance().getBaseContext(), ""+message, Toast.LENGTH_SHORT).show();
    }
}
