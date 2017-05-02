package com.comeet;

import com.comeet.exchange.ExchangeResourceException;
import com.comeet.utilities.ApiLogger;
import com.comeet.utilities.ExchangeUtility;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import junit.framework.Assert;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.property.complex.Attendee;

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
    
    private String username = "";
    private String password = "";
    
   
    /**
     * Checks that the location name is properly set according to the room.
     */
    @Test
    public void setLocationGsdTest() throws Exception {
        
        if (username.length() == 0) {
            System.out.println("Test did not proceed: No username and password.");
            return;
        }
        
        String room = "gsdloebStudyB@meetl.ink";
        
        // create appointment with one person
        ExchangeService service = ExchangeUtility.getExchangeService(username, password);
        Appointment appt = new Appointment(service);
        appt.getRequiredAttendees().add(new Attendee(room));
        appt.save();
        
        RoomsDao rd = new RoomsDao(service);
        rd.setLocationName(appt, room);
        
        Assert.assertTrue(appt.getLocation().equals("[GSD] [Loeb] Study Room B"));
        
        // delete test appointment
        appt.delete(DeleteMode.HardDelete);
        
    }

    /**
     * Checks that the location is correctly set from the room's email. 
     */
    @Test
    public void setLocationTest() throws Exception {
        
        if (username.length() == 0) {
            System.out.println("Test did not proceed: No username and password.");
            return;
        }
        
        String room = "CambMa1Story305@meetl.ink";
        
        // create appointment with one person
        ExchangeService service = ExchangeUtility.getExchangeService(username, password);
        Appointment appt = new Appointment(service);
        appt.getRequiredAttendees().add(new Attendee(room));
        appt.save();
        
        RoomsDao rd = new RoomsDao(service);
        rd.setLocationName(appt, room);
        
        
        Assert.assertTrue(appt.getLocation().equals("[HES][OneStorySt] Room 305"));
        
        // delete test appointment
        appt.delete(DeleteMode.HardDelete);
        
        
    }
    
    
    
    /**
     * Test that the look-up email method works when using an Attendee's address.
     */
    @Test
    public void lookUpEmailTest() throws Exception {
        
        if (username.length() == 0) {
            System.out.println("Test did not proceed: No username and password.");
            return;
        }
        
        // create appointment with one person
        ExchangeService service = ExchangeUtility.getExchangeService(username, password);
        Appointment appt = new Appointment(service);
        appt.getRequiredAttendees().add(new Attendee("CambMa1Story305@meetl.ink"));
        appt.save();
        
        // retrieve the appointment
        Appointment retrieved = Appointment.bind(service, appt.getId(),
                        new PropertySet(BasePropertySet.FirstClassProperties));
        Attendee attend = retrieved.getRequiredAttendees().getItems().get(0);
        
        RoomsDao rd = new RoomsDao(service);
        
        // test that lookUpEmail works correctly
        String result = rd.lookUpEmailAddress(attend.getAddress());
        Assert.assertTrue(result.equals("CambMa1Story305@meetl.ink"));
        
        // delete test appointment
        appt.delete(DeleteMode.HardDelete);
    }
    

    /**
     * Check that the lookup email method works with an address from GSD.
     */
    @Test
    public void lookUpGsdEmailTest() throws Exception {
        
        if (username.length() == 0) {
            System.out.println("Test did not proceed: No username and password.");
            return;
        }
        
        // create appointment with one person
        ExchangeService service = ExchangeUtility.getExchangeService(username, password);
        Appointment appt = new Appointment(service);
        appt.getRequiredAttendees().add(new Attendee("gsdloebStudyA@meetl.ink"));
        appt.save();
        
        // retrieve the appointment
        Appointment retrieved = Appointment.bind(service, appt.getId(),
                        new PropertySet(BasePropertySet.FirstClassProperties));
        Attendee attend = retrieved.getRequiredAttendees().getItems().get(0);
        
        
        RoomsDao rd = new RoomsDao(service);
        
        // test that lookUpEmail works correctly
        String result = rd.lookUpEmailAddress(attend.getAddress());
        Assert.assertTrue(result.equals("gsdloebStudyA@meetl.ink"));
        
        // delete test appointment
        appt.delete(DeleteMode.HardDelete);
        
    }
    
    
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
