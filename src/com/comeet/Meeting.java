package com.comeet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "meeting") 

public class Meeting {

    
    private String subject = "";
    private String body;
    private Date start;
    private Date end;
    private String location;
    
    private Room room;
    
    private Person meetingcreator;
    private List<Person> requiredattendees;
    private List<Person> optionalattendees;
    

    // constructor
    
    Meeting() {

    }

    // setters
    
    @XmlElement 
    public void setSubject(String subject) {
        this.subject = subject;
    }

    
    @XmlElement
    public void setStart(Date start) {
        this.start = start;
    }

    @XmlElement 
    public void setEnd(Date end) {
        this.end = end;
    }
    
    @XmlElement 
    public void setLocation(String location) {
        this.location = location;
    }

    
    
    @XmlElement
    public void setBody(String body) {
        this.body = body;
    }

    @XmlElement
    public void setMeetingcreator(Person person) {
        this.meetingcreator = person;
    }
    
    @XmlElement
    public void setRequiredattendees(List<Person> people) {
        this.requiredattendees = people;
    }

    @XmlElement
    public void setOptionaldattendees(List<Person> people) {
        this.optionalattendees = people;
    }

    @XmlElement
    public void setRoom(Room room) {
        this.room = room;
    }
    
    
    // getters
    public Room getRoom() {
        return room;
    }
    
    
    public String getStart() {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(start) ;
    }

    public String getEnd() {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(end) ;
    }

    public String getLocation() {
        return location;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
    
    
    public Person getMeetingcreator() {
        return meetingcreator;
    }
    
    public List<Person> getRequiredattendees() {
        return requiredattendees;
    }

    public List<Person> getOptionaldattendees() {
        return optionalattendees;
    }
    
    
}
