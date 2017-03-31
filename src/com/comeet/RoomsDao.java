
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
import java.util.List;
import java.util.TimeZone;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceRequestException;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceResponseException;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

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
                    ServiceResponseException, ExchangeServiceException {

        List<EmailAddress> names = new ArrayList<EmailAddress>();

        try {
            EmailAddressCollection c = service.getRoomLists();
            for (EmailAddress e : c) {

                Collection<EmailAddress> rooms = service.getRooms(e);

                for (EmailAddress r : rooms) {
                    System.out.println(r.toString());
                    System.out.println(r.getAddress());
                    System.out.println(r.getName());
                    names.add(r);
                }
            }
        } catch (ServiceRequestException e) {
            throw e;
        } catch (ServiceResponseException e) {
            throw e;
        } catch (Exception e) {
            throw new ExchangeServiceException(e);
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

        try {
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
        } catch (IOException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw e;
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
     * @param room The room of interest, to be filled in.
     */
    public static void retrieveMetadata(Room room) throws Exception {
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
                                               ServiceResponseException, ExchangeServiceException {

        List<EmailAddress> names = null;

        try {
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
        } catch (ServiceRequestException e) {
            throw e;
        } catch (ServiceResponseException e) {
            throw e;
        } catch (Exception e) {
            throw new ExchangeServiceException(e);
        }
        return names;
    }
    
    /**
     * Gets all rooms at the organization.
     * 
     * @return A list of rooms.
     */
    public List<Room> getBuildingRooms(String buildingEmail) throws Exception {
        
        List<EmailAddress> rooms = null;
        try {
            rooms = getBuildingRoomlist(buildingEmail);
        } catch (Exception ex) {
            throw ex;
        }

        List<Room> roomList = null;
        try {
            roomList = new ArrayList<Room>();
            for (EmailAddress s : rooms) {
                Room room = new Room();
                room.setName(s.getName());
                room.setEmail(s.getAddress());
                retrieveMetadata(room);
                roomList.add(room);
            }
        } catch (IOException e) {
            throw e;
        } catch (ClassNotFoundException e) {
            throw e;
        }
        return roomList;
    }
}
