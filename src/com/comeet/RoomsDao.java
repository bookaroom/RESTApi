
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

    /**
     * Creates an appointment.
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

        // ?start=2017-05-23|9:00:00&end=2017-05-23|9:00:00&
        // subject=testSubject&body=testBody&recipients=CambMa1Story305@meetl.ink,jablack@meetl.ink

        Appointment appointment = new Appointment((new ExchangeConnection()).getService());
        appointment.setSubject(subject);
        appointment.setBody(MessageBody.getMessageBodyFromText(body));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");
        Date startDate = formatter.parse(start); // "2017-05-23|5:00:00");
        Date endDate = formatter.parse(end); // "2017-05-23|6:00:00");
        appointment.setStart(startDate);
        appointment.setEnd(endDate);

        appointment.setRecurrence(null);

        for (String s : recips) {
            appointment.getRequiredAttendees().add(s);
        }
        // appointment.getRequiredAttendees().add("CambMa1Story305@meetl.ink");
        // appointment.getRequiredAttendees().add("jablack@meetl.ink");

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
     * Gets all rooms at the organization.
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
                roomList = (List<Room>) ois.readObject();
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

    public static void retrieveMetadata(Room room) {
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
