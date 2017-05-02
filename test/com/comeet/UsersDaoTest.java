package com.comeet;

import com.comeet.utilities.ExchangeUtility;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.property.complex.Attendee;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import org.junit.Test;

public class UsersDaoTest {

    
    // Instructions: Fill in username and password with the
    // login information such jack black's email and password.
    // Otherwise, the test will not run and a message will print.
    
    private String username = "";
    private String password = "";
    
    
    
    
    /**
     * Checks that meeting attendees are properly converted to Person objects.
     */
    @Test
    public void attendeesToPeopleJackAdminTest() throws Exception {
        
        if (username.length() == 0) {
            System.out.println("Test did not proceed: No username and password.");
            return;
        }
        
        ExchangeService service = ExchangeUtility.getExchangeService(username, password);
        Appointment appt = new Appointment(service);
        appt.getRequiredAttendees().add(new Attendee("jablack@meetl.ink"));
        appt.getRequiredAttendees().add(new Attendee("adminish@meetl.ink"));
        appt.save();
        
        Appointment retrieved = Appointment.bind(service, appt.getId(),
                        new PropertySet(BasePropertySet.FirstClassProperties));
        UsersDao ud = new UsersDao(service);
        Meeting meet = new Meeting();
        
        List<Person> list = ud.attendees(meet, appt, retrieved.getRequiredAttendees());
        List<String> emails = new ArrayList<String>();
        for (Person p: list) {
            emails.add(p.getEmail());
        }
        
        Assert.assertTrue(emails.contains("jablack@meetl.ink"));
        Assert.assertTrue(emails.contains("adminish@meetl.ink"));
        
        appt.delete(DeleteMode.HardDelete);

    }
    



    /**
     * Checks that the required attendees function populates a meeting object.
     */
    @Test
    public void requiredAttendeesTest() throws Exception {
        
        if (username.length() == 0) {
            System.out.println("Test did not proceed: No username and password.");
            return;
        }

        ExchangeService service = ExchangeUtility.getExchangeService(username, password);
        Appointment appt = new Appointment(service);
        appt.getRequiredAttendees().add(new Attendee("jiwhite@meetl.ink"));
        appt.getRequiredAttendees().add(new Attendee("adminish@meetl.ink"));
        appt.save();
        
        Appointment retrieved = Appointment.bind(service, appt.getId(),
                        new PropertySet(BasePropertySet.FirstClassProperties));
        UsersDao ud = new UsersDao(service);
        
        Meeting meet = new Meeting();
        ud.requiredAttendees(meet, retrieved);
        
        List<String> emails = new ArrayList<String>();
        for (Person p: meet.getRequiredattendees()) {
            emails.add(p.getEmail());
        }
        Assert.assertTrue(emails.contains("jiwhite@meetl.ink"));
        Assert.assertTrue(emails.contains("adminish@meetl.ink"));
        Assert.assertFalse(emails.contains("jablack@meetl.ink"));
        
        appt.delete(DeleteMode.HardDelete);
        
    }

    
    /**
     * Checks that the optional attendees function populates a meeting object.
     */
    @Test
    public void optionalAttendeesTest() throws Exception {
        
        if (username.length() == 0) {
            System.out.println("Test did not proceed: No username and password.");
            return;
        }
        
        ExchangeService service = ExchangeUtility.getExchangeService(username, password);
        Appointment appt = new Appointment(service);
        appt.getOptionalAttendees().add(new Attendee("jablack@meetl.ink"));
        appt.getOptionalAttendees().add(new Attendee("adminish@meetl.ink"));
        appt.save();
        
        Appointment retrieved = Appointment.bind(service, appt.getId(),
                        new PropertySet(BasePropertySet.FirstClassProperties));
        UsersDao ud = new UsersDao(service);
        
        Meeting meet = new Meeting();
        ud.optionalAttendees(meet, retrieved);
        List<String> emails = new ArrayList<String>();
        
        for (Person p: meet.getOptionalattendees()) {
            emails.add(p.getEmail());
        }

        Assert.assertFalse(emails.contains("jiwhite@meetl.ink"));
        Assert.assertTrue(emails.contains("adminish@meetl.ink"));
        Assert.assertTrue(emails.contains("jablack@meetl.ink"));
        
        appt.delete(DeleteMode.HardDelete);
        
    }

    
    /**
     * Checks that the room data is correctly retrieved with populateRoomData.
     */
    @Test
    public void populateRoomDataTest() throws Exception {
        
        if (username.length() == 0) {
            System.out.println("Test did not proceed: No username and password.");
            return;
        }
        
        ExchangeService service = ExchangeUtility.getExchangeService(username, password);
        Appointment appt = new Appointment(service);
        appt.setLocation("[HES][OneStorySt] Room 305");
        
        List<Person> people = new ArrayList<Person>();
        Person person = new Person();
        person.setEmail("CambMa1Story305@meetl.ink");
        person.setName("[HES][OneStorySt] Room 305");
        people.add(person);
        
        Meeting meet = new Meeting();
        meet.setRequiredattendees(people);
        
        UsersDao ud = new UsersDao(service);
        ud.populateRoomData(meet, appt);
        
        meet.getRoom().getName().equals("[HES][OneStorySt] Room 305");
        meet.getRoom().getEmail().equals("CambMa1Story305@meetl.ink");
        meet.getRoom().getAddress().equals("1 Story St, Cambridge, MA 02143");   
    }
    
    /**
     * Checks that the meeting body is correctly retrieved by appointmentBody.
     */
    @Test
    public void meetingBodyTest() throws Exception {
        
        if (username.length() == 0) {
            System.out.println("Test did not proceed: No username and password.");
            return;
        }
        
        String randomBody = "random meetingBodyTest body.";
        
        ExchangeService service = ExchangeUtility.getExchangeService(username, password);
        Appointment appt = new Appointment(service);
        appt.setBody(new MessageBody(randomBody));
        appt.save();
        
        PropertySet ps = new PropertySet();
        ps.setRequestedBodyType(BodyType.Text);
        ps.setBasePropertySet(BasePropertySet.FirstClassProperties);
        Appointment retrieved = Appointment.bind(service, appt.getId(),ps);
        
        UsersDao ud = new UsersDao(service);
        String body = ud.appointmentBody(retrieved);
        Assert.assertTrue(body.trim().equals(randomBody));
        
        appt.delete(DeleteMode.HardDelete);

    }


    /**
     * Checks that people are correctly read from an AttendeeColletion to a Meeting.
     */
    @Test
    public void attendeesToPeopleJillJackTest() throws Exception {
        
        if (username.length() == 0) {
            System.out.println("Test did not proceed: No username and password.");
            return;
        }
        
        ExchangeService service = ExchangeUtility.getExchangeService(username, password);
        Appointment appt = new Appointment(service);
        appt.getRequiredAttendees().add(new Attendee("jiwhite@meetl.ink"));
        appt.getRequiredAttendees().add(new Attendee("jablack@meetl.ink"));
        appt.save();
        
        Appointment retrieved = Appointment.bind(service, appt.getId(),
                        new PropertySet(BasePropertySet.FirstClassProperties));
        
        UsersDao ud = new UsersDao(service);
        Meeting meet = new Meeting();

        List<Person> list = ud.attendees(meet, appt, retrieved.getRequiredAttendees());
        List<String> emails = new ArrayList<String>();
        for (Person p: list) {
            emails.add(p.getEmail());
        }
        
        Assert.assertTrue(emails.contains("jiwhite@meetl.ink"));
        Assert.assertTrue(emails.contains("jablack@meetl.ink"));
        
        appt.delete(DeleteMode.HardDelete);

    }


}
