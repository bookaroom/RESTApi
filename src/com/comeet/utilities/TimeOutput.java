package com.comeet.utilities;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.joda.time.DateTime;

public class TimeOutput {

    

    
    
    /**
     * Convert date object to time format for the iOS client.
     * 
     */
    public static String dateToString(Date date) {

        Instant i = date.toInstant().truncatedTo(ChronoUnit.SECONDS);

        DateTimeFormatter f = DateTimeFormatter.ISO_INSTANT;

        return f.format(i);
        
    }
    
    
    /**
     * Convert to datetime to date and then convert to string.
     * 
     */
    public static String dateTimeToString(DateTime dt) {
        return dateToString(dt.toDate());
    }
    
}
