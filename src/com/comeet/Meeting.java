package com.comeet;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;


@XmlRootElement(name = "meeting")

public class Meeting {
    private DateTime startTime;
    private int duration;
    private List<String> attendees;
    private Room room;
    private String title;
    private String message;

    // constructor
    Meeting() {
        attendees = new ArrayList<>();
        room = new Room();
    }

    // setters
    public void setStartTime(DateTime meetingTime) {
        this.startTime = meetingTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setLocation(Room room) {
        this.room = room;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setAttendee(String attendee) {
        attendees.add(attendee);
    }

    // getters
    public DateTime getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    Room getLocation() {
        return room;
    }

    String getTitle() {
        return title;
    }
}
