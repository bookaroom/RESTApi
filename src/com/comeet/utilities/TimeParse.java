package com.comeet.utilities;

import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class TimeParse {
    
    
    public TimeParse(){}
    
    
    private Date startDate;
    private Date endDate;
    
    /**
     * Get start date.
     * 
     */
    public Date getStart() {
        
        return startDate;
        
    }
    
    /**
     * Get end date.
     */
    public Date getEnd() {
        
        return endDate;
    }
    
    
    /**
     * Makes date objects.
     * 
     * @param start - start time
     * @param end - end time
     */
    public void parse(String start, String end) throws NullPointerException {
        DateTime startTime = null;
        DateTime endTime = null;

        DateTimeFormatter fmt = ISODateTimeFormat.dateTimeNoMillis();

        if (start.isEmpty()) {
            startTime = DateTime.now();
            endTime = startTime.plusDays(7);
        } else {
            startTime = fmt.parseDateTime(start);
        }

        if (endTime == null) {
            if (end.isEmpty()) {
                endTime = startTime.plusDays(7);
            } else {
                endTime = fmt.parseDateTime(end);
            }
        } 

        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start Time greater than End Time");
        }
        
        startDate = startTime.toDate();
        endDate = endTime.toDate();
            
    }  

}
