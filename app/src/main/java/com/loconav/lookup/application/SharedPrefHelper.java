package com.loconav.lookup.application;

import android.content.Context;
import android.content.SharedPreferences;

import static com.loconav.lookup.Constants.LOOK_UP_PREFERENCES;

/**
 * Created by prateek on 25/06/18.
 */

public class SharedPrefHelper {

    private Context context;

    public static SharedPrefHelper getNewInstance(Context context) {
        return new SharedPrefHelper(context);
    }

    private SharedPrefHelper(Context context) {
        this.context = context;
    }

    private SharedPreferences getSharedPf() {
        return context.getSharedPreferences(LOOK_UP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void setData(String key, String value) {
        SharedPreferences.Editor editor = getSharedPf().edit().putString(key, value);
        editor.apply();
    }

    public void setData(String key, int value) {
        SharedPreferences.Editor editor = getSharedPf().edit().putInt(key, value);
        editor.apply();
    }

    public void setData(String key, boolean value){
        SharedPreferences.Editor editor = getSharedPf().edit().putBoolean(key, value);
        editor.apply();
    }

    public String getStringData(String key) {
        return getSharedPf().getString(key, "");
    }

    public int getIntData(String key) {
        return getSharedPf().getInt(key, -1);
    }

    public boolean getBooleanData(String key){
        return getSharedPf().getBoolean(key, false);
    }
}
