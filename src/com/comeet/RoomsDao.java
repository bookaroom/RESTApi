
package com.comeet;

import com.comeet.data.DataRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceResponseException;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

public class RoomsDao {

    /*
     * static class RedirectionUrlCallback implements IAutodiscoverRedirectionUrl { public boolean
     * autodiscoverRedirectionUrlValidationCallback( String redirectionUrl) { return
     * redirectionUrl.toLowerCase().startsWith("https://"); } }
     */

    /**
     * Gets the Exchange ews service.
     * 
     * @return Ews Service connection.
     * @throws URISyntaxException When the hardcoded uri is malformed.
     */
    public ExchangeService getService() throws URISyntaxException {
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
        ExchangeCredentials credentials = new WebCredentials("adminish@meetl.ink", "Springe599");
        service.setCredentials(credentials);
        service.setUrl(new URI("https://outlook.office365.com/EWS/Exchange.asmx"));

        return service;

    }

    /**
     * Creates a new calendar event.
     * 
     * @return A list of meetings including the created event.
     * @throws ServiceResponseException When the service didn't like the event creation.
     * @throws Exception When the service failed initial connection.
     */
    public List<Meeting> createEvent() throws ServiceResponseException, Exception {

        Appointment appointment = new Appointment(getService());
        appointment.setSubject("Test Subject");
        appointment.setBody(MessageBody.getMessageBodyFromText("Test Body"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = formatter.parse("2017-05-23 1:00:00");
        Date endDate = formatter.parse("2017-05-23 3:00:00");
        appointment.setStart(startDate);
        appointment.setEnd(endDate);

        try {
            appointment.setRecurrence(null);
        } catch (Exception e) {
            throw new RuntimeException("Setting recurrence to null should never cause a problem.");
        }

        appointment.getRequiredAttendees().add("CambMa1Story305@meetl.ink");
        appointment.getRequiredAttendees().add("jablack@meetl.ink");

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
    public List<EmailAddress> getRoomsList() throws Exception {
        List<EmailAddress> names = new ArrayList<EmailAddress>();

        try (ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2)) {

            ExchangeCredentials credentials = new WebCredentials("jablack@meetl.ink", "Comeet599");
            service.setCredentials(credentials);
            service.setUrl(new URI("https://outlook.office365.com/EWS/Exchange.asmx"));

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
        }

        return names;
    }

    /**
     * Gets all rooms at the orgaization.
     * 
     * @return A list of rooms.
     */
    public List<Room> getAllRooms() {
        List<EmailAddress> rooms = null;
        try {
            rooms = getRoomsList();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        List<Room> roomList = null;
        try {
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
                Object obj = ois.readObject();
                if (obj instanceof List<?>) {
                    roomList = (List<Room>) obj;
                }
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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

    private void retrieveMetadata(Room room) {
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
}
