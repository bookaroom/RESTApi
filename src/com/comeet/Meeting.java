package com.comeet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "meeting") 

public class Meeting {

    private String id = "";
    private String subject = "";
    private String body = "";
    private Date start = new Date();
    private Date end = new Date();
    private String location = "";
    private Room room = new Room();
    private Person meetingcreator = new Person();
    private List<Person> requiredattendees = new ArrayList<Person>();
    private List<Person> optionalattendees = new ArrayList<Person>();
    

    // constructor
    
    Meeting() {

    }

    // setters
    @XmlElement 
    public void setId(String id) {
        this.id = id;
    }
    
    @XmlElement 
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    
    @XmlElement
    public void setBody(String body) {
        this.body = body;
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
    public void setRoom(Room room) {
        this.room = room;
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
    
    
    // getters

    public String getId() {
        return id;
    }
    
    
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

    public List<Person> getOptionalattendees() {
        return optionalattendees;
    }
    
    
}
