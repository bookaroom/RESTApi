package com.comeet;

import com.comeet.exchange.ExchangeResourceException;
import com.comeet.utilities.ApiLogger;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;



@RunWith(MockitoJUnitRunner.class)
public class RoomsDaoTest {
    
    
    @InjectMocks 
    RoomsDao dao;
    
    @Mock
    ApiLogger log;
    
    @Before
    public void setUp() throws IOException {
        //ApiLogger.setup();
    }
    
    //GetCriteria tests
    //Note: This method calls a data repository class method therefore
    //      there are limited number of tests 
    
    /**
     * Check that class type of the return object on a successful call 
     * for getCriteria.
     */
    @Test
    public void getCriteriaClassTest() throws Exception {
        Assert.assertSame(ArrayList.class, 
                       dao.getCriteria("meetl.ink")
                        .getClass());
    }
    
    //getBuildingsRooms tests
    
    /**
     * Tests date format for start and end string.
     */
    @Test(expected = Exception.class)
    public void getBuildingRoomsSizeTest() throws InvalidParameterException, 
                                                    ExchangeResourceException,Exception {
        
        String start = DateTime.now().toDateTimeISO().toString();
        String end = DateTime.now().plusDays(7).toDateTimeISO().toString();
        Assert.assertSame(false, 
                        dao.getBuildingRooms("Bldg_CambMaOneStorySt@meetl.ink",start,end)
                        .isEmpty());
    }
    
    /**
     * Test for invalid email address.
     */
    @Test(expected = InvalidParameterException.class)
    public void getBuildingRoomsEmailTest() throws InvalidParameterException, 
                                               ExchangeResourceException,Exception {
        DateTimeFormatter fmt = ISODateTimeFormat.dateTimeNoMillis();
        
        String start = fmt.print(DateTime.now().toDateTimeISO());
        String end = fmt.print(DateTime.now().plusDays(7).toDateTimeISO());
        Assert.assertSame(ArrayList.class, 
                        dao.getBuildingRooms("Bldg_CambMaOneStoryStmeetl.ink",start,end)
                        .getClass());
        
        Assert.assertSame(ArrayList.class, 
                        dao.getBuildingRooms("Bldg_CambMaOneStorySt@meetlink",start,end)
                        .getClass());
    }
    
    /**
     * Test for ISO dates.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getBuildingRoomsIsoDateTest() throws Exception {
        String start = DateTime.now().toDateTimeISO().toString();
        String end = DateTime.now().plusDays(7).toDateTimeISO().toString();
        
        Assert.assertSame(Exception.class, 
                        dao.getBuildingRooms("Bldg_CambMaOneStorySt@meetl.ink",start,end)
                        .getClass());
        
        Assert.assertSame(Exception.class, 
                        dao.getBuildingRooms("Bldg_CambMaOneStorySt@meetl.ink","",end)
                        .getClass());
        
        Assert.assertSame(Exception.class, 
                        dao.getBuildingRooms("Bldg_CambMaOneStorySt@meetl.ink",start,"")
                        .getClass());
    }
}
