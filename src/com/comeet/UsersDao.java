package com.comeet;

import com.comeet.utilities.ApiLogger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.ResolveNameSearchLocation;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.misc.NameResolution;
import microsoft.exchange.webservices.data.misc.NameResolutionCollection;
import microsoft.exchange.webservices.data.property.complex.Attendee;
import microsoft.exchange.webservices.data.property.complex.AttendeeCollection;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;

// BUGBUG: [BOOKAROOM-45] https://bookaroom.atlassian.net/browse/BOOKAROOM-45
//CHECKSTYLE DISABLE: JavadocMethod
public class UsersDao {
    
    /**
     * The Exchange service is injected via the constructor.
     */
    private ExchangeService service;

    /**
     * Constructs a Users Data Access Object.
     * @param service The exchange service to access for User data.
     */
    public UsersDao(ExchangeService service) {
        this.service = service;
    }
    
    private List<String> getApptAttendees(Appointment appt) throws ServiceLocalException {
        List<String> myAttendees = new ArrayList<String>();
        AttendeeCollection ac = appt.getOptionalAttendees();
        for (Attendee a : ac) {
            myAttendees.add(a.getAddress());
        }

        ac = appt.getRequiredAttendees();
        for (Attendee a : ac) {
            myAttendees.add(a.getAddress());
        }

        return myAttendees;
    }

    public String lookUpEmailAddress(String address) throws Exception {
        NameResolutionCollection nrc =
                        service.resolveName(address, ResolveNameSearchLocation.DirectoryOnly, true);
        Iterator<NameResolution> i = nrc.iterator();
        NameResolution nr = i.next();
        return nr.getMailbox().getAddress();

    }

    public List<Person> attendees(Meeting m, Appointment appt, AttendeeCollection ac) throws Exception {
        List<Attendee> list = ac.getItems();

        List<Person> people = new ArrayList<Person>();

        for (Attendee a : list) {

            Person p = new Person();
            p.setName(a.getName());
            p.setEmail(lookUpEmailAddress(a.getAddress()));
            people.add(p);
        }

        return people;
    }

    public void requiredAttendees(Meeting m, Appointment appt)
                    throws Exception {

        AttendeeCollection ac = appt.getRequiredAttendees();
        m.setRequiredattendees(attendees(m, appt, ac));
        
    }

    public void optionalAttendees(Meeting m, Appointment appt)
                    throws Exception {
        AttendeeCollection ac = appt.getOptionalAttendees();
        m.setOptionaldattendees(attendees(m, appt, ac));
    }
    
    public Room populateRoomData(List<Person> attendees, Appointment appt)
                    throws ServiceLocalException, Exception {

        Room theRoom = new Room();
        RoomsDao roomDao = new RoomsDao(null);
        
        String email = "";
        boolean found = false;
        for (Person p: attendees) {
            if (p.getName().equals(appt.getLocation())) {
                email = p.getEmail();
                found = true;
            }
        }

        theRoom.setEmail(email);
        theRoom.setName(appt.getLocation());
        
        if (found) {
            roomDao.retrieveMetadata(theRoom);        
        }
        
        return theRoom;
    }


    public Person meetingCreator(Appointment appt) throws Exception {
        Person creator = new Person();
        EmailAddress organizer = appt.getOrganizer();
        creator.setEmail(lookUpEmailAddress(organizer.getAddress()));
        creator.setName(organizer.getName());

        return creator;
    }

    public String appointmentBody(Appointment appt) {

        String body = "";

        try {
            MessageBody mesBod = appt.getBody();
            if (mesBod != null) {
                body = mesBod.toString();
            }
        } catch (Exception e) {
            ApiLogger.logger.log(Level.SEVERE, "Failure getting appointment body", e);
        }

        return body;
    }


    public List<Appointment> findAppointments(String start, String end) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date startDate = formatter.parse(start);// "2010-05-01 12:00:00");
        Date endDate = formatter.parse(end); // "2010-05-30 13:00:00");

        CalendarFolder cf = CalendarFolder.bind(service, WellKnownFolderName.Calendar);
        FindItemsResults<Appointment> findResults =
                        cf.findAppointments(new CalendarView(startDate, endDate));
        
        return findResults.getItems();
    }

    /**
     * Gets meetings.
     * 
     * @param start Start of query range.
     * @param end End of query range
     * @return List of meetings.
     * @throws Exception If something went wrong.
     */
    public List<Meeting> getUserMeetings(String start, String end) throws Exception {

        List<Meeting> meetings = new ArrayList<Meeting>();
        List<Appointment> results = findAppointments(start, end);

        for (Appointment appt : results) {
            if (appt != null) {
                Meeting m = new Meeting();
                m.setStart(appt.getStart());
                m.setEnd(appt.getEnd());
                m.setBody(appointmentBody(appt));
                m.setSubject(appt.getSubject());
                m.setLocation(appt.getLocation());
                m.setMeetingcreator(meetingCreator(appt));
                Appointment attAppt = Appointment.bind(service, appt.getId(),
                                new PropertySet(BasePropertySet.FirstClassProperties));
                requiredAttendees(m, attAppt);
                optionalAttendees(m, attAppt);
                m.setRoom(populateRoomData(m.getRequiredattendees(), appt));
                meetings.add(m);
            }
        }

        return meetings;
    }



}
