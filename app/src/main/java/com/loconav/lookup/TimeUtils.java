package com.loconav.lookup;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String getDate(String timeStamp){
        long timestamp2;
        if(timeStamp!=null)
            timestamp2= Long.parseLong(timeStamp);
        else
            return "xx";

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date netDate = (new Date(timestamp2));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }
}
