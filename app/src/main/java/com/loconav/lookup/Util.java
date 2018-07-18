package com.loconav.lookup;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sejal on 17-07-2018.
 */

public class Util {

   static Boolean matchregex(String str,String str2){

        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(str2);
        Boolean ab= matcher.find();
        return ab;
    }
}
