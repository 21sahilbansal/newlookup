package com.loconav.lookup.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String getDate(String timeStamp){
        long timestamp2;
        try{
            timestamp2 = Long.parseLong(timeStamp);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date netDate = (new Date(timestamp2));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }
}
