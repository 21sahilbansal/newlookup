package com.loconav.lookup.application;

import android.content.Context;
import android.content.SharedPreferences;

import static com.loconav.lookup.Constants.LOOK_UP_PREFERENCES;

/**
 * Created by sejal on 25-06-2018.
 */

public class SharedPrefHelper {

    private static SharedPrefHelper sharedPrefHelper;

    public static SharedPrefHelper getInstance() {
        if(sharedPrefHelper == null)
            sharedPrefHelper = new SharedPrefHelper();
        return sharedPrefHelper;
    }

    private SharedPrefHelper(){}

    private SharedPreferences getSharedPref(){
        return LookUpApplication.getInstance().getSharedPreferences(LOOK_UP_PREFERENCES, Context.MODE_PRIVATE);
    }


    public void setStringData(String key, String value) {
      SharedPreferences.Editor editor = getSharedPref().edit().putString(key,value);
      editor.apply();
    }

    public void setLongData(String key, Long value) {
        SharedPreferences.Editor editor = getSharedPref().edit().putLong(key,value);
        editor.apply();
    }
    public void setIntData(String key, Integer value) {
        SharedPreferences.Editor editor=getSharedPref().edit().putInt(key,value);
        editor.apply();
    }
    public void setBooleanData(String key, Boolean value) {
        SharedPreferences.Editor editor = getSharedPref().edit().putBoolean(key,value);
        editor.apply();
    }
    public String getStringData(String key) {
        return getSharedPref().getString(key,"");
    }
    public Integer getIntData(String key) {
        return getSharedPref().getInt(key,-1);
    }
    public Boolean getBooleanData(String key) {
        return getSharedPref().getBoolean(key,false);
    }
    public Long getLongData(String key) {
        return getSharedPref().getLong(key,-1);
    }

    public void removeStringData(String key) {
        SharedPreferences.Editor editor=getSharedPref().edit();
        editor.remove(key);
        editor.apply();
    }
    public void clearAllData(){
        SharedPreferences.Editor editor=getSharedPref().edit();
        editor.clear();
    }

}