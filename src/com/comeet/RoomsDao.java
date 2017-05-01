
package com.comeet;

import com.comeet.data.DataRepository;
import com.comeet.exchange.ExchangeResourceException;
import com.comeet.exchange.ExchangeServiceException;
import com.comeet.utilities.ApiLogger;
import com.comeet.utilities.TimeParse;
import com.comeet.utilities.Validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.availability.AvailabilityData;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.search.ResolveNameSearchLocation;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceRequestException;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceResponseException;
import microsoft.exchange.webservices.data.core.response.AttendeeAvailability;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.misc.NameResolution;
import microsoft.exchange.webservices.data.misc.NameResolutionCollection;
import microsoft.exchange.webservices.data.misc.availability.AttendeeInfo;
import microsoft.exchange.webservices.data.misc.availability.GetUserAvailabilityResults;
import microsoft.exchange.webservices.data.misc.availability.TimeWindow;
import microsoft.exchange.webservices.data.property.complex.Attendee;
import microsoft.exchange.webservices.data.property.complex.AttendeeCollection;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.availability.CalendarEvent;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class RoomsDao {

    private static final String CLASS_NAME = RoomsDao.class.getName();

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
     * Look up email address from encoded string
     * 
     * @param address - email address string
     * @return actual email address
     * @throws Exception //TODO: For unknown reasons.
     */
    public String lookUpEmailAddress(String address) throws Exception {
        NameResolutionCollection nrc =
                        service.resolveName(address, ResolveNameSearchLocation.DirectoryOnly, true);
        Iterator<NameResolution> i = nrc.iterator();
        NameResolution nr = i.next();
        return nr.getMailbox().getAddress();
    }

    /**
     * Sets the location name of the appointment
     * 
     * @param appointment - appointment
     * @param roomRecipient - room
     * @throws Exception //TODO: For unknown reasons.
     */
    public void setLocationName(Appointment appointment, String roomRecipient)
                    throws ServiceLocalException, Exception {

        String name = "";
        // System.out.println(appointment.getId() + " id\n");
        Appointment attAppt = Appointment.bind(service, appointment.getId(),
                        new PropertySet(BasePropertySet.FirstClassProperties));
        AttendeeCollection ac = attAppt.getRequiredAttendees();
        for (Iterator<Attendee> iterator = ac.iterator(); iterator.hasNext();) {
            Attendee a = iterator.next();
            if (lookUpEmailAddress(a.getAddress()).equals(roomRecipient)) {
                name = a.getName();
            }
        }

        appointment.setLocation(name);
        appointment.update(ConflictResolutionMode.AlwaysOverwrite);

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
                    List<String> recips, String roomRecipient, List<String> optional)
                    throws ServiceResponseException, Exception {

        Appointment appointment = new Appointment(service);
        appointment.setSubject(subject);
        appointment.setBody(MessageBody.getMessageBodyFromText(body));

        TimeParse tp = new TimeParse();
        tp.parse(start, end);
        Date startDate = tp.getStart();
        Date endDate = tp.getEnd();
        
        
        appointment.setStart(startDate);
        appointment.setEnd(endDate);

        appointment.setRecurrence(null);

        for (String s : recips) {
            appointment.getRequiredAttendees().add(s);
        }
        appointment.getRequiredAttendees().add(roomRecipient);

        for (String s : optional) {
            if (s.trim().length() > 0) {
                appointment.getOptionalAttendees().add(s);
            }
        }
        
        appointment.save();
        setLocationName(appointment, roomRecipient);

        return new ArrayList<Meeting>();
    }



    /**
     * Gets a list of room email addresses.
     * 
     * @return List of room email addresses.
     * @throws Exception When the service fails to be created.
     */
    public List<EmailAddress> getRoomsList() throws ServiceRequestException,
                    ServiceResponseException, ExchangeServiceException, Exception {

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
                populateMetadata(room);
                roomList.add(room);
            }

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
     * Fetches a room's metadata.
     * 
     * @param room The room of interest, to be filled in.
     */
    public void populateMetadata(Room room) {
        DataRepository db = new DataRepository();

        try {
            Room metadata = db.retrieveRoomMetadata(room.getEmail());
    
            if (metadata != null) {
                room.setCapacity(metadata.getCapacity());
                room.setCountry(metadata.getCountry());
                room.setNavigationMap(metadata.getNavigation());
                room.setLatitude(metadata.getLatitude());
                room.setCapacity(metadata.getCapacity());
                room.setLongitude(metadata.getLongitude());
                room.setMetroarea(metadata.getMetro());
                room.setState(metadata.getState());
                room.setRoomPic(metadata.getPicture());
                room.addAmenities(metadata.getAmenities());
                
            }
        } catch (SQLException sqle) {
            // Log the problem and continue gracefully.
            String err = String.format("Failed to retrieve metadata for room {0}", room.getEmail());
            ApiLogger.logger.warning(err);
        }
    }

    /**
     * Implementing method to get the search criteria for a given domain
     * 
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
     * Retrieve the rooms in a selected building from exchange.
     * 
     * @param email Email address of the building list to retrieve.
     * @return A list of rooms' email addresses.
     * @throws ExchangeResourceException When retrieving the resource from exchange fails.
     */
    private List<EmailAddress> getRoomsFromRoomlist(String email) throws ExchangeResourceException {
        final String methodName = "getRoomsFromRoomlist";
        ApiLogger.logger.entering(CLASS_NAME, methodName);

        EmailAddress roomEmail = new EmailAddress(email);

        try {
            Collection<EmailAddress> rooms = service.getRooms(roomEmail);
            ApiLogger.logger.exiting(CLASS_NAME, methodName, rooms);
            return new ArrayList<>(rooms);
        } catch (Exception e) {
            String err = String.format("Failed to get rooms from roomlist {0}", email);
            ApiLogger.logger.severe(err);
            throw new ExchangeResourceException(err, e);
        }
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
    public List<Room> getBuildingRooms(String roomlistEmail, String start, String end)
                    throws ExchangeResourceException,InvalidParameterException, Exception {

        Date startDate = null;
        Date endDate = null;

        TimeParse parseTime = new TimeParse();
        parseTime.parse(start, end);
        startDate = parseTime.getStart();
        endDate = parseTime.getEnd();

        if (!Validator.validateEmail(roomlistEmail)) {
            throw new InvalidParameterException(
                            String.format("{0} is an invalid email address", roomlistEmail));
        }
            
        List<EmailAddress> roomEmails = getRoomsFromRoomlist(roomlistEmail);

        List<Room> rooms = new ArrayList<Room>();

        for (EmailAddress contact : roomEmails) {
            Room room = new Room();
            room.setName(contact.getName());
            room.setEmail(contact.getAddress());
            populateMetadata(room);
            rooms.add(room);
        }

        TimeWindow duration = new TimeWindow(startDate, endDate);
        populateAvailability(rooms, duration);
        
        return rooms;
    }

    /**
     * Populates room availability.
     * @param rooms A list of rooms to populate.
     * @param duration The duration to populate.
     * @throws ExchangeResourceException If the operation does not complete.  Room calendars may be partially populated.
     */
    private void populateAvailability(List<Room> rooms, TimeWindow duration) throws ExchangeResourceException {

        List<AttendeeInfo> attendees = new ArrayList<>();
        for (Room room : rooms) {
            attendees.add(new AttendeeInfo(room.getEmail()));
        }
        
        GetUserAvailabilityResults response;
        try {
            response = service.getUserAvailability(attendees, duration, AvailabilityData.FreeBusy);
        } catch (Exception e) {
            String err = String.format("Failed to retrieve user {0} availability at times {1}", attendees, duration);
            ApiLogger.logger.warning(err);
            throw new ExchangeResourceException(err, e);
        }
        
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        
        // From https://msdn.microsoft.com/en-us/library/aa564001(v=exchg.150).aspx:
        // The availability information for each user appears in a unique FreeBusyResponse element.
        // The order of users in the GetUserAvailability operation request determines the order of availability data for each user in the response.
        Iterator<Room> roomIterator = rooms.iterator(); 
        for (AttendeeAvailability attendeeCalendar : response.getAttendeesAvailability()) {
            
            if (!roomIterator.hasNext()) {
                throw new ExchangeResourceException("Availability result does not match request: Too many attendeeCalendars!");
            }
            
            Room room = roomIterator.next();
            for (CalendarEvent evnt : attendeeCalendar.getCalendarEvents()) {
                room.addFreeBusyTime(
                            // TODO: Are these times pre-biased for timezones?
                            new FreeBusySlot(fmt.print(new DateTime(evnt.getStartTime())),
                                            fmt.print(new DateTime(evnt.getEndTime())),
                                            evnt.getFreeBusyStatus().name()));
            }
        }
    }
}
