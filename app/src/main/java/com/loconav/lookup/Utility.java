package com.loconav.lookup;

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

}
