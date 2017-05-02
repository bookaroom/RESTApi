package com.comeet;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestAttendees {
    Attendees attendees = new Attendees();
    private ArrayList<Person> people = new ArrayList<Person>();
    private Person hugo = new Person();
    private Person paco = new Person();

    @Before
    public void addPeople() {
        people.add(hugo);
        people.add(paco);
    }

    @Test
    public void testGetRequiredAttendees() {

        attendees.setRequiredattendees(people);
        assertEquals(people, attendees.getRequiredattendees());
    }

    @Test
    public void testGetOptionaAttendees() {
        attendees.setOptionalattendees(people);
        assertEquals(people, attendees.getOptionalattendees());
    }


}
