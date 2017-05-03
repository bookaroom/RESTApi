package com.comeet;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class TestMeeting {



    private List<Person> attendees = new ArrayList<Person>();
    private Meeting meeting = new Meeting();
    
    
    @Test
    public void testGetId(){
        meeting.setId("ID");
        assertEquals("ID", meeting.getId());
    }
    
    @Test
    public void testGetSubject(){
        meeting.setSubject("Subject");
        assertEquals("Subject", meeting.getSubject());
    }
    
    @Test
    public void testGetBody(){
        meeting.setBody("Body");
        assertEquals("Body", meeting.getBody());
    }
    
    @Test
    public void testGetStart(){
        meeting.setStart(new Date((long) 12345.12));
        assertEquals(new Date((long) 12345.12), meeting.getStart());
    }
    
    @Test
    public void testGetEnd(){
        meeting.setEnd(new Date((long) 12345.12));
        assertEquals(new Date((long) 12345.12), meeting.getEnd());
    }
    
   
    @Test
    public void testGetLocation(){
        meeting.setLocation("Location");
        assertEquals("Location", meeting.getLocation());
    }
    
    @Test
    public void testGetRoom(){
        Room room = new Room();
        meeting.setRoom(room);
        assertEquals(room, meeting.getRoom());
    }

    @Test
    public void testGetMeetingCreator(){
        Person hugo = new Person();
        meeting.setMeetingcreator(hugo);
        assertEquals(hugo, meeting.getMeetingcreator());
    }
    
    @Test
    public void testGetRequiredAteendees(){
        Person paco = new Person();
        attendees.add(paco);
        meeting.setRequiredattendees(attendees);
        assertEquals(attendees, meeting.getRequiredattendees());
    }
    
    
    @Test
    public void testGetOptionalAteendees(){
        Person paco = new Person();
        attendees.add(paco);
        meeting.setOptionaldattendees(attendees);
        assertEquals(attendees, meeting.getOptionalattendees());
    }
}
