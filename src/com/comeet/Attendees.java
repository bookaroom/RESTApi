package com.comeet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "attendees") 

public class Attendees {
    

    private List<Person> requiredattendees = new ArrayList<Person>();
    private List<Person> optionalattendees = new ArrayList<Person>();
    

    // constructor
    
    Attendees() {

    }

    @XmlElement
    public void setRequiredattendees(List<Person> people) {
        this.requiredattendees = people;
    }

    @XmlElement
    public void setOptionaldattendees(List<Person> people) {
        this.optionalattendees = people;
    }

    
    public List<Person> getRequiredattendees() {
        return requiredattendees;
    }

    public List<Person> getOptionalattendees() {
        return optionalattendees;
    }
    
    
}