package com.comeet.utilities;

import com.comeet.utilities.TimeParse;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Assert;
import org.junit.Test;

public class TimeParseTest {
   
    TimeParse parseTime;
    
    /**
     * Test to verify that the date follows ISO format
     * @throws Exception
     */
    @Test(expected=Exception.class)
    public void TimeParseISOTest() throws Exception
    {
        String startDate = DateTime.now().toString();
        String endDate = DateTime.now().toString();
        parseTime = new TimeParse();
        
        parseTime.parse(startDate, endDate);
    }
    
    /**
     * Test output of the Timeparse class parse method 
     * @throws Exception
     */
    @Test
    public void TimeParseDateTest() throws Exception
    {
        //setup of data
        DateTime start, end;
        parseTime = new TimeParse();
        
        start = DateTime.now();
        end = DateTime.now().plusDays(4);
        
        DateTimeFormatter fmt = ISODateTimeFormat.dateTimeNoMillis();
        
        String startDate = fmt.print(start);
        String endDate = fmt.print(end);
        
        //process data
        parseTime.parse(startDate, endDate);
        Date startResult = parseTime.getStart();
        Date endResult = parseTime.getEnd();
        
        //test
        Assert.assertEquals(startResult.toString(), start.toDate().toString());
        Assert.assertEquals(endResult.toString(), end.toDate().toString());
        
        //process data - empty start date
        parseTime.parse("", endDate);
        startResult = parseTime.getStart();
        endResult = parseTime.getEnd();
        
        //test
        Assert.assertEquals(startResult.toString(), DateTime.now().
                        toDate().toString());
        Assert.assertEquals(endResult.toString(), DateTime.now().
                        plusDays(7).toDate().toString());
        
        //process data - empty end date
        parseTime.parse(startDate, "");
        startResult = parseTime.getStart();
        endResult = parseTime.getEnd();
        
        //test
        Assert.assertEquals(startResult.toString(), DateTime.now().
                        toDate().toString());
        Assert.assertEquals(endResult.toString(), DateTime.now().
                        plusDays(7).toDate().toString());
        
        //process data - empty start, end date
        parseTime.parse("", "");
        startResult = parseTime.getStart();
        endResult = parseTime.getEnd();
        
        //test
        Assert.assertEquals(startResult.toString(), DateTime.now().
                        toDate().toString());
        Assert.assertEquals(endResult.toString(), DateTime.now().
                        plusDays(7).toDate().toString());
        
        //process data - empty start, end date
        end = DateTime.now().plusDays(10);
        endDate = fmt.print(end);
        parseTime.parse(startDate, endDate);
        startResult = parseTime.getStart();
        endResult = parseTime.getEnd();
        
        //test
        Assert.assertEquals(startResult.toString(), DateTime.now().
                        toDate().toString());
        Assert.assertEquals(endResult.toString(), DateTime.now().
                        plusDays(10).toDate().toString());
        
    }
    
    /**
     * Test passing start time greater than end time
     */
    @Test(expected=IllegalArgumentException.class)
    public void TimeParserExceptionTest()
    {
        //setup of data
        DateTime start, end;
        parseTime = new TimeParse();
        
        start = DateTime.now().plusDays(4);
        end = DateTime.now();
        
        DateTimeFormatter fmt = ISODateTimeFormat.dateTimeNoMillis();
        
        String startDate = fmt.print(start);
        String endDate = fmt.print(end);
        
        //process data
        parseTime.parse(startDate, endDate);
    }
}
