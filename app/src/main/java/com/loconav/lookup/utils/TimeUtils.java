package com.loconav.lookup.utils;

import java.security.spec.ECParameterSpec;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

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

    public static Long getEpochTime(String timestamp) throws ParseException{
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


    /**
     *
     * @param epochTime time sould be in second not in millisecond
     * @return
     */
    public static String getTimeFromEpochTime(String epochTime){
       long longEpochTime  = Long.parseLong(epochTime);
        Date date = new Date(longEpochTime*1000);
       SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
        dateformat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        String formatted = dateformat.format(date);
       return formatted;

    }
}