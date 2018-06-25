package com.loconav.lookup;

import com.loconav.lookup.login.model.User;

import static com.loconav.lookup.Constants.AUTH_TOKEN;
import static com.loconav.lookup.Constants.ID;
import static com.loconav.lookup.Constants.LOCATION;
import static com.loconav.lookup.Constants.NAME;
import static com.loconav.lookup.Constants.PHONE_NUMBER;
import static com.loconav.lookup.Constants.USERNAME;
import static com.loconav.lookup.application.LookUpApplication.editor;
import static com.loconav.lookup.application.LookUpApplication.sharedPreferences;

/**
 * Created by prateek on 13/06/18.
 */

public class SharedPrefHelper {
    public static void saveUser(User user) {
        saveStringData(USERNAME, user.getName());
        saveIntData(ID, user.getId());
        saveStringData(NAME, user.getName());
        saveStringData(PHONE_NUMBER, user.getPhoneNumber());
        saveStringData(LOCATION, user.getLocation());
        saveStringData(AUTH_TOKEN, user.getAuthenticationToken());
    }


    public static void saveIntData(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public static void saveStringData(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveBooleanData(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }


    public static int getIntData(String key) {
        return sharedPreferences.getInt(key, -1);
    }


    public static String getStringData(String key) {
        return sharedPreferences.getString(key, "");
    }

    public static boolean getBooleanData(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

}
