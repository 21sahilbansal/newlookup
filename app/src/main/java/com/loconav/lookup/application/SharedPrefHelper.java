package com.loconav.lookup.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;

import static com.loconav.lookup.Constants.LOOK_UP_PREFERENCES;

/**
 * Created by sejal on 25-06-2018.
 */

public class SharedPrefHelper {
    private Context context;

    public static SharedPrefHelper getInstance(Context context) {
        return new SharedPrefHelper(context);
    }

    private SharedPrefHelper(Context context){
     this.context=context;
    }

    private SharedPreferences getSharedPref(){
        return context.getSharedPreferences(LOOK_UP_PREFERENCES, Context.MODE_PRIVATE);
    }


    public void setStringData(String key, String value) {
      SharedPreferences.Editor editor=getSharedPref().edit().putString(key,value);
      editor.apply();
    }
    public void setIntData(String key, Integer value) {
        SharedPreferences.Editor editor=getSharedPref().edit().putInt(key,value);
        editor.apply();
    }
    public void setBooleanData(String key, Boolean value) {
        SharedPreferences.Editor editor=getSharedPref().edit().putBoolean(key,value);
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
}

