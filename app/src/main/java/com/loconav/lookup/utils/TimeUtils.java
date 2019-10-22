package com.loconav.lookup.utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class TimeUtils {
    public static String getDate(String timeStamp){
        long timestamp2;
        try{
            timestamp2 = Long.parseLong(timeStamp);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date netDate = (new Date(timestamp2));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

    public static Long getEpochTime(String timestamp){
            if(timestamp == null) return null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                Date dt = sdf.parse(timestamp);
                long epoch = dt.getTime();
                return epoch;
            } catch(ParseException e) {
                return null;
            }
        }

    }

