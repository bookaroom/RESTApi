
package com.comeet;

import com.comeet.data.DataRepository;
import com.comeet.exchange.ExchangeServiceException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.availability.AvailabilityData;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceRequestException;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceResponseException;
import microsoft.exchange.webservices.data.core.response.AttendeeAvailability;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.misc.availability.AttendeeInfo;
import microsoft.exchange.webservices.data.misc.availability.GetUserAvailabilityResults;
import microsoft.exchange.webservices.data.misc.availability.TimeWindow;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.availability.CalendarEvent;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class RoomsDao {

    /**
     * The Exchange service is injected via the constructor.
     */
    protected ExchangeService service;

    /**
     * Constructs a rooms Data Access Object.
     * 
     * @param service The exchange service to access for Room data.
     */
    public RoomsDao(ExchangeService service) {
        this.service = service;
    }

    /**
     * Creates an appointment.
     * 
     * @param start Appointment start time.
     * @param end Appointment end time.
     * @param subject The subject line.
     * @param body Body text for the appointment
     * @param recips Recipient list.
     * @return The meeting(s) created.
     * @throws ServiceResponseException When the exchange service bails.
     * @throws Exception //TODO: For unknown reasons.
     */
    public List<Meeting> makeAppointment(String start, String end, String subject, String body,
                    List<String> recips) throws ServiceResponseException, Exception {

        Appointment appointment = new Appointment(service);
        appointment.setSubject(subject);
        appointment.setBody(MessageBody.getMessageBodyFromText(body));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date startDate = formatter.parse(start); // "2017-05-23|5:00:00");
        Date endDate = formatter.parse(end); // "2017-05-23|6:00:00");
        appointment.setStart(startDate);
        appointment.setEnd(endDate);

        appointment.setRecurrence(null);

        for (String s : recips) {
            appointment.getRequiredAttendees().add(s);
        }

        appointment.save();

        List<Meeting> list = new ArrayList<Meeting>();

        return list;
    }



    /**
     * Gets a list of room email addresses.
     * 
     * @return List of room email addresses.
     * @throws Exception When the service fails to be created.
     */
    public List<EmailAddress> getRoomsList() throws ServiceRequestException,
                    ServiceResponseException, ExchangeServiceException,Exception {

        List<EmailAddress> names = new ArrayList<EmailAddress>();

        EmailAddressCollection c = service.getRoomLists();
        for (EmailAddress e : c) {

            Collection<EmailAddress> rooms = service.getRooms(e);

            for (EmailAddress r : rooms) {
                names.add(r);
            }
        }

        return names;
    }

    /**
     * Gets all rooms at the organization.
     * 
     * @return A list of rooms or null on error.
     * @throws ServiceRequestException When the exchange service's request was invalid or malformed.
     * @throws ServiceResponseException When the exchange server's response was invalid or
     *         malformed.
     * @throws ExchangeServiceException When something else went wrong with the service.
     */
    public List<Room> getAllRooms() throws ServiceResponseException, ServiceRequestException,
                    ExchangeServiceException, IOException, ClassNotFoundException, Exception {
        List<EmailAddress> rooms = null;
        List<Room> roomList = null;

        rooms = getRoomsList();

        File file = new File("Roo.dat");
        file.delete();

        if (!file.exists()) {
            roomList = new ArrayList<Room>();

            for (EmailAddress s : rooms) {
                Room room = new Room();
                room.setName(s.getName());
                room.setEmail(s.getAddress());
                retrieveMetadata(room);
                roomList.add(room);
            }

            // User user = new User(1, "Peter", "Teacher");

            // userList.add(user);
            saveRoomList(roomList);
        } else {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            roomList = (List<Room>) ois.readObject();
            ois.close();
        }

        return roomList;
    }

    private void saveRoomList(List<Room> roomList) {
        try {
            File file = new File("Roo.dat");
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(roomList);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches a room metadata.
     * @param room room The room of interest, to be filled in.
     * @throws Exception throws an exception
     */
    public void retrieveMetadata(Room room) throws Exception {
        DataRepository db = new DataRepository();

        Room metadata = db.retrieveRoomMetadata(room.getEmail());

        if (metadata != null) {
            room.setCapacity(metadata.getCapacity());
            room.setCountry(metadata.getCountry());
            room.setBuilding(metadata.getBuilding());
            room.setNavigationMap(metadata.getNavigationMap());
            room.setLatitude(metadata.getLatitude());
            room.setCapacity(metadata.getCapacity());
            room.setLongitude(metadata.getLongitude());
            room.setMetroarea(metadata.getMetroarea());
            room.setState(metadata.getState());
            room.setRoomPic(metadata.getRoomPic());
        }
    }
    
    /**
     * Implementing method to get the search criteria for a given domain
     * @param domain domain of organization to search for
     * @return The list of metro areas and their room list names
     * @throws Exception On an unexpected error.
     */ 
    public List<MetroBuildingList> getCriteria(String domain) throws Exception {
        DataRepository db = new DataRepository();
        List<MetroBuildingList> result = db.retrieveSearchCriteria(domain);
        
        return result;
    }
    
    
    /**
     * retrieve the rooms in a selected building from exchange
     * @param email email address of the building list to retrieve
     * @return The list of metro areas and their room list names
     * @throws Exception On an unexpected error.
     */ 
    private List<EmailAddress> getBuildingRoomlist(String email) throws ServiceRequestException, 
                                               ServiceResponseException, ExchangeServiceException,
                                                                           Exception {

        List<EmailAddress> names = null;

        EmailAddressCollection c = service.getRoomLists();
        for (EmailAddress e : c) {
            if (e.getAddress().equalsIgnoreCase(email)) {
                
                names = new ArrayList<EmailAddress>();
                Collection<EmailAddress> rooms = service.getRooms(e);
       
                for (EmailAddress r : rooms) {
                    names.add(r);
                }
            }
        }

        return names;
    }
    
    /**
     * Gets all rooms at the organization.
     * 
     * @param roomlistEmail the email of the room list to retrieve
     * @param start the start time for the search window
     * @param end the end time for the search window
     * @return A list of rooms
     * @throws Exception throws an exception 
     */
    public List<Room> getBuildingRooms(String roomlistEmail, String start, String end) throws Exception {
        
        DateTime startTime = null;
        DateTime endTime = null;
        
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
 
        if (start.isEmpty()) {
            startTime = DateTime.now();
            endTime = DateTime.now().plusDays(7);
        } else {
            startTime = fmt.parseDateTime(start);
            fmt.parseDateTime(end);
        }
        
        List<EmailAddress> rooms = getBuildingRoomlist(roomlistEmail);
        List<AttendeeInfo> attendeeInfo = new ArrayList();

        List<Room> roomList = new ArrayList<Room>();
        for (EmailAddress s : rooms) {
            Room room = new Room();
            room.setName(s.getName());
            room.setEmail(s.getAddress());
            retrieveMetadata(room);
            roomList.add(room);
            attendeeInfo.add(new AttendeeInfo(s.getAddress()));
        }
        
        List<AttendeeAvailability> result = getFreeBusyBlocks(attendeeInfo,
                        startTime, endTime);
        System.out.println(result.size());
        Iterator iter = result.iterator();
        if (result.size() == roomList.size()) {
            for (Room room : roomList) {
                AttendeeAvailability availability = (AttendeeAvailability) iter.next();
                
                for (CalendarEvent evnt :availability.getCalendarEvents()) {
                    room.addFreeBusyTime(new FreeBusySlot(
                                    fmt.print(new DateTime(evnt.getStartTime())),
                                    fmt.print(new DateTime(evnt.getEndTime())),
                                    evnt.getFreeBusyStatus().name()));
                }
            } 
        } else {
            throw new Exception("Unable to retrieve all room calendars");
        }  
        
        return roomList;
    }
    
    /**
     * Get free/busy information for rooms.  
     * @param meetingAttendees A list of attendees to get free/busy blocks for
     * @param startTime DateTime object of representing the start time 
     * @param endTime DateTime object of representing the end time
     * @return List of Attendee Availability objects
     * @throws Exception throws an exception
     */
    private List<AttendeeAvailability> getFreeBusyBlocks(List<AttendeeInfo> meetingAttendees, 
                    DateTime startTime, DateTime endTime) throws Exception {
        
        List<AttendeeAvailability> freeBusyInfo = new ArrayList();
        
        TimeWindow timeFrame = new TimeWindow();
        timeFrame.setStartTime(startTime.toDate());
        timeFrame.setEndTime(endTime.toDate());
        GetUserAvailabilityResults response = 
                        service.getUserAvailability(meetingAttendees, timeFrame, AvailabilityData.FreeBusy);
        
        for (AttendeeAvailability timeSlot:response.getAttendeesAvailability()) {
            freeBusyInfo.add(timeSlot);
        }
        
        return freeBusyInfo;
    }
}
