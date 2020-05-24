package com.imandroid.financemanagement.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TextHelper {

    public static String getDateReadable(Date now,String date) {
        String resultDate = "";
        try {
            Date formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(date);
            long millisSinceExp = now.getTime() - formattedDate.getTime();
            long minSinceExp = TimeUnit.MILLISECONDS.toMinutes(millisSinceExp);
            long hoursSinceExp = TimeUnit.MILLISECONDS.toHours(millisSinceExp);
            if (minSinceExp < 60l) {
                if (minSinceExp == 1) resultDate = minSinceExp + " minute ago";
                else resultDate = minSinceExp + " minutes ago";
            }
            else if (hoursSinceExp < 24l) {
                if (hoursSinceExp == 1) resultDate = hoursSinceExp + " hour ago";
                else resultDate = hoursSinceExp + " hours ago";
            }
            else resultDate = new SimpleDateFormat("dd MMM yyyy HH:mm").format(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }
}
