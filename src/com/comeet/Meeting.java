package com.comeet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "meeting") 

public class Meeting {
    private Date startTime;
    private int duration;
    private List<String> attendees;
    private String location;
    private String title;
    private String message;

    // constructor
    
    Meeting() {
        attendees = new ArrayList<>();
    }

    // setters
    
    @XmlElement
    public void setStartTime(Date meetingTime) {
        this.startTime = meetingTime;
    }

    @XmlElement 
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    @XmlElement 
    public void setLocation(String location) {
        this.location = location;
    }

    @XmlElement 
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAttendee(String attendee) {
        attendees.add(attendee);
    }
    
    @XmlElement 
    public void setAttendees(List<String> attendees) {
        this.attendees = attendees;
    }
    
    @XmlElement
    public void setMessage(String message) {
        this.message = message;
    }

    // getters
    
    public Date getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    String getLocation() {
        return location;
    }

    String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
    
    public List<String> getAttendees() {
        return attendees;
    }
}
