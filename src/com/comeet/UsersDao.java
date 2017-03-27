package com.comeet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import microsoft.exchange.webservices.data.property.complex.EmailAddressDictionary;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;



public class UsersDao {
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

    public String lookUpEmailAddress(ExchangeService service, String address) throws Exception{

        NameResolutionCollection nrc = service.resolveName(address, ResolveNameSearchLocation.DirectoryOnly, true);
        Iterator<NameResolution> i = nrc.iterator();
        NameResolution nr = i.next();
        return nr.getMailbox().getAddress();
        
    }
    
    
    
    public List<Person> attendees(ExchangeService service, Meeting m, Appointment appt, AttendeeCollection ac) throws Exception{
        List<Attendee> list = ac.getItems();

        //System.out.println("size: " + ac.getCount() + "\n");

        List<Person> people = new ArrayList<Person>();
        
        for(Attendee a: list){
            
            Person p = new Person();
            p.setName(a.getName());
            p.setEmail(lookUpEmailAddress(service, a.getAddress()));
            people.add(p);
        }
        
        return people;
    }
    
    
    public void requiredAttendees(ExchangeService service, Meeting m, Appointment appt) throws Exception{        
        
        AttendeeCollection ac = appt.getRequiredAttendees();
        
        m.setRequiredattendees(attendees(service, m, appt, ac));
        
    }
    
    
    public void optionalAttendees(ExchangeService service, Meeting m, Appointment appt) throws Exception{
        AttendeeCollection ac = appt.getOptionalAttendees();
        m.setOptionaldattendees(attendees(service, m, appt, ac));
    }
    
    
    public Map<String, String> roomMap(ExchangeService service) throws Exception{
        Map<String, String> roomMap = new HashMap<String, String>();
        EmailAddressCollection roomLists = service.getRoomLists();
        List<EmailAddress> roomListsTwo = roomLists.getItems();
        for(EmailAddress e: roomListsTwo){
            Collection<EmailAddress> col = service.getRooms(e);
            
            for(EmailAddress add: col){
                roomMap.put(add.getName(), add.getAddress());
            }
            
        }
        
        return roomMap;
    }
    
    
    public Room populateRoomData(Map<String, String> roomMap, Appointment appt) throws ServiceLocalException{
        
        Room theRoom = new Room();
        String roomEmail = roomMap.get(appt.getLocation());
        theRoom.setEmail(roomEmail);
        RoomsDao.retrieveMetadata(theRoom);
        theRoom.setName(appt.getLocation());
        
        return theRoom;
    }
    
    
    public Person meetingCreator(ExchangeService service, Appointment appt) throws Exception{
        Person creator = new Person();
        EmailAddress organizer = appt.getOrganizer();
        creator.setEmail(lookUpEmailAddress(service, organizer.getAddress()));
        creator.setName(organizer.getName());
        
        return creator;
    }
    
    public String appointmentBody(Appointment appt){

        String body = "";
        
        try{
            MessageBody mesBod = appt.getBody();
            if(mesBod != null){
                body = mesBod.toString();
            }
        }catch(Exception e){}
        
        return body;
    }
    
    
    public List<Appointment> findAppointments(ExchangeService service, String start, String end) throws Exception{
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");
        Date startDate = formatter.parse(start);// "2010-05-01 12:00:00");
        Date endDate = formatter.parse(end); // "2010-05-30 13:00:00");
        CalendarFolder cf = CalendarFolder.bind(service, WellKnownFolderName.Calendar);
        FindItemsResults<Appointment> findResults =
                        cf.findAppointments(new CalendarView(startDate, endDate));
        
       return findResults.getItems();
    }

    /**
     * Gets meetings.
     * @param start Start of query range.
     * @param end End of query range
     * @return List of meetings.
     * @throws Exception If something went wrong.
     */
    public List<Meeting> getUserMeetings(String start, String end) throws Exception {

        ExchangeService service = (new ExchangeConnection()).getService();

        List<Meeting> meetings = new ArrayList<Meeting>();
        Map<String, String> roomMap = roomMap(service);
        
        List<Appointment> results = findAppointments(service, start, end);

        for (Appointment appt : results) {
            if (appt != null) {
                Meeting m = new Meeting();

                m.setStart(appt.getStart());
                m.setEnd(appt.getEnd());
                m.setBody(appointmentBody(appt));
                m.setSubject(appt.getSubject());                
                m.setLocation(appt.getLocation());
                m.setMeetingcreator(meetingCreator(service, appt));
                m.setRoom(populateRoomData(roomMap, appt));

                Appointment attAppt = Appointment.bind(service, appt.getId(), new PropertySet(BasePropertySet.FirstClassProperties) );
                requiredAttendees(service, m, attAppt);
                optionalAttendees(service, m, attAppt);

                meetings.add(m);
            }

        }

        return meetings;
    }



}
