package com.comeet;

import com.comeet.auth.AuthContextFactory;
import com.comeet.exchange.ExchangeClientException;
import com.comeet.exchange.ExchangeServiceFactory;
import com.comeet.exchange.ExchangeServiceFactoryImpl;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType; 

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceResponseException;

@Path("")
public class UserService {

    AuthContextFactory authFactory = new AuthContextFactory();

    ExchangeServiceFactory serviceFactory = new ExchangeServiceFactoryImpl();

    /**
     * The implementing method for GET /rooms
     * 
     * @return A list of room objects.
     */
    @GET
    @Path("/rooms")
    @Produces("application/json")
    public List<Room> getRooms(@Context HttpHeaders headers) {

        try {
            serviceFactory.setAuthContext(authFactory.buildContext(headers));

            // Each call should define new instance of Service and DAO object
            try (ExchangeService service = serviceFactory.create()) {
                RoomsDao roomsDao = new RoomsDao(service);
                return roomsDao.getAllRooms();
            }
        } catch (Exception e) {
            // TODO Respond with appropriate HTTP code and json error detail.
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Implementing method for GET /user/meetings
     * <p>
     * Example URL:
     * http://localhost:8080/JavaApplication/user/meetings?start=2017-03-25*12:00:00&end=2017-06-25*12:00:00
     * </p>
     * 
     * @param start Start of query range.
     * @param end End of query range.
     * @return Meeting info within the query range.
     * @throws ServiceResponseException If the result is not 200 OK
     * @throws Exception On an unexpected error.
     */
    @GET
    @Path("/user/meetings")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
    public List<Meeting> getUserMeetings(@Context HttpHeaders headers,
                    @DefaultValue("") @QueryParam("start") String start,
                    @DefaultValue("") @QueryParam("end") String end)
                    throws ServiceResponseException, Exception {

        try {
            serviceFactory.setAuthContext(authFactory.buildContext(headers));

            // Each call should define new instance of Service and DAO object
            try (ExchangeService service = serviceFactory.create()) {
                UsersDao ud = new UsersDao(service);
                return ud.getUserMeetings(start, end);
            }
        } catch (ExchangeClientException e) {
            // TODO Respond with appropriate HTTP code and json error detail.
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * Implementing method for POST /rooms/reserve
     * <p>
     * Example URL: http://localhost:8080/JavaApplication/rooms/reserve start=2017-03-10*9:00:00
     * end=2017-03-10*10:00:00 subject=testSubject body=testBody
     * recipients=CambMa1Story305@meetl.ink,jablack@meetl.ink
     * </p>
     * 
     * @param start Start of the reservation.
     * @param end End of the reservation.
     * @param subject Subject of the meeting.
     * @param body Body text for the meeting.
     * @param recipients Recipients of the meeting.
     * @return The meeting(s) created.
     * @throws ServiceResponseException If the result is not 200 OK.
     * @throws Exception On an unexpected error.
     */
    @POST
    @Path("/rooms/reserve")
    @Produces("application/json")
    public List<Meeting> getRoomsTwo(@Context HttpHeaders headers,
                    @DefaultValue("") @FormParam("start") String start,
                    @DefaultValue("") @FormParam("end") String end,
                    @DefaultValue("") @FormParam("subject") String subject,
                    @DefaultValue("") @FormParam("body") String body,
                    @DefaultValue("") @FormParam("recipients") String recipients)
                    throws ServiceResponseException, Exception {

        try {
            serviceFactory.setAuthContext(authFactory.buildContext(headers));

            // Each call should define new instance of Service and DAO object
            try (ExchangeService service = serviceFactory.create()) {
                RoomsDao roomsDao = new RoomsDao(service);
                List<String> recips = Arrays.asList(recipients.split("\\s*,\\s*"));
                return roomsDao.makeAppointment(start, end, subject, body, recips);
            }
        } catch (ExchangeClientException e) {
            // TODO Respond with appropriate HTTP code and json error detail.
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}

/*
 * @GET
 * 
 * @Path("/meetings/userid") //userid is an email
 * 
 * @Produces("application/json") // @Produces(MediaType.APPLICATION_XML) public List<Room>
 * getUserMeetings() { MeetingsDao meetingsDao = new MeetingsDao(); return
 * meetingsDao.getUserMeetings(String email);
 * 
 * }
 */