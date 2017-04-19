package com.comeet;

import com.comeet.auth.AuthContextException;
import com.comeet.auth.AuthContextFactory;
import com.comeet.exchange.ExchangeClientException;
import com.comeet.exchange.ExchangeServiceFactory;
import com.comeet.exchange.ExchangeServiceFactoryImpl;
import com.comeet.utilities.ApiLogger;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceResponseException;

@Path("")
public class UserService {

    AuthContextFactory authFactory = new AuthContextFactory();

    ExchangeServiceFactory serviceFactory = new ExchangeServiceFactoryImpl();

    private static final String WWW_AUTHENTICATE = "WWW-Authenticate";

    private static final String BEARER_CHALLENGE = "Bearer scope=\"comeet\"";
    
    private static final String BASIC_CHALLENGE = "Basic";
    
    private static final String RECIPIENT_LIST_REGEX = "\\s*,\\s*";

    /**
     * The implementing method for GET /rooms
     * Note: Obsolete. Only used for testing.
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
        } catch (AuthContextException e) {
            // TODO Use OAuth2 for real.
            e.printStackTrace();
            throw new WebApplicationException(buildBearerChallenge(e).build());
        } catch (Exception e) {
            // TODO Respond with appropriate HTTP code and json error detail.
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Implementing method for GET /{organization}/user/{user}/meetings
     * <p>
     * Example URL:
     * http://localhost:8080/comeet/pfizer.com/user/angelo@pfizer.com/meetings?start=2017-03-25T12:00:00Z&end=2017-06-25T12:00:00Z
     * </p>
     * 
     * @param orgDomain Start of query range.
     * @param user Start of query range.
     * @param start Start of query range.
     * @param end End of query range.
     * @return Meeting info within the query range.
     * @throws ServiceResponseException If the result is not 200 OK
     * @throws Exception On an unexpected error.
     */
    @GET
    @Path("/{orgDomain}/meetings")
    @Produces("application/json")
    public List<Meeting> getUserMeetings(@Context HttpHeaders headers,
                    @PathParam("orgDomain") String orgDomain, @PathParam("user") String user,
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
        } catch (AuthContextException e) {
            // TODO Use OAuth2 for real.
            e.printStackTrace();
            throw new WebApplicationException(buildBearerChallenge(e).build());
        } catch (ExchangeClientException e) {
            // TODO Respond with appropriate HTTP code and json error detail.
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * Implementing method for GET /{organization}/meetings/attendees?id=
     * <p>
     * Example URL:
     * </p>
     * 
     * @param orgDomain Start of query range.
     * @param id - meeting ID
     * @return Meeting info within the query range.
     * @throws ServiceResponseException If the result is not 200 OK
     * @throws Exception On an unexpected error.
     */
    @POST
    @Path("/{orgDomain}/meetings/attendees")
    @Produces("application/json")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Attendees getAttendees(@Context HttpHeaders headers,
                    @PathParam("orgDomain") String orgDomain, 
                    @DefaultValue("") @FormParam("id") String id)
                    throws ServiceResponseException, Exception {

        try {
            serviceFactory.setAuthContext(authFactory.buildContext(headers));

            // Each call should define new instance of Service and DAO object
            try (ExchangeService service = serviceFactory.create()) {
                UsersDao ud = new UsersDao(service);
                return ud.getAttendees(id);
            }
        } catch (AuthContextException e) {
            // TODO Use OAuth2 for real.
            e.printStackTrace();
            throw new WebApplicationException(buildBearerChallenge(e).build());
        } catch (ExchangeClientException e) {
            // TODO Respond with appropriate HTTP code and json error detail.
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }    


    /**
     * Implementing method for POST /{organization}/rooms/{roomRecipient}/reserve"
     * <p>
     * Example URL: http://localhost:8080/comeet/pfizer.com/rooms/100Main605@pfizer.com/reserve
     * start=2017-03-10*9:00:00Z end=2017-03-10T10:00:00Z subject=testSubject body=testBody
     * recipients=jablack@meetl.ink
     * </p>
     * 
     * @param start Start of the reservation.
     * @param end End of the reservation.
     * @param subject Subject of the meeting.
     * @param body Body text for the meeting.
     * @param requiredRecipients Recipients of the meeting (required).
     * @param optionalRecipients Required recipients of the meeting (optional).
     * @return The meeting(s) created.
     * @throws ServiceResponseException If the result is not 200 OK.
     * @throws Exception On an unexpected error.
     */
    @POST
    @Path("/{orgDomain}/rooms/{roomRecipient}/reserve")
    @Produces("application/json")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public List<Meeting> reserveRoom(@Context HttpHeaders headers,
                    @PathParam("orgDomain") String orgDomain,
                    @PathParam("roomRecipient") String roomRecipient,
                    @FormParam("subject") String subject, @FormParam("body") String body,
                    @FormParam("start") String start, @FormParam("end") String end,
                    @DefaultValue("") @FormParam("required") String requiredRecipients,
                    @DefaultValue("") @FormParam("optional") String optionalRecipients)
                    throws ServiceResponseException, Exception {
        
        try {
            serviceFactory.setAuthContext(authFactory.buildContext(headers));

            // Each call should define new instance of Service and DAO object
            try (ExchangeService service = serviceFactory.create()) {
                RoomsDao roomsDao = new RoomsDao(service);
                List<String> recips = Arrays.asList(requiredRecipients.split(RECIPIENT_LIST_REGEX));
                return roomsDao.makeAppointment(start, end, subject, body, recips, roomRecipient);
            }
        } catch (AuthContextException e) {
            // TODO Use OAuth2 for real.
            e.printStackTrace();
            throw new WebApplicationException(buildBearerChallenge(e).build());
        } catch (ExchangeClientException e) {
            // TODO Respond with appropriate HTTP code and json error detail.
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Implementing method for GET /{organization}/metros
     * <p>
     * Example URL: http://localhost:8080/pfizer.com/metros
     * </p>
     * 
     * @param orgdomain domain of organization to search for
     * @return The list of metro areas and the room lists they contain.
     * @throws ServiceResponseException If the result is not 200 OK.
     * @throws Exception On an unexpected error.
     */
    @GET
    @Path("/{orgdomain}/metros")
    @Produces("application/json")
    public List<MetroBuildingList> getMetroAreas(@Context HttpHeaders headers,
                    @PathParam("orgdomain") String orgdomain) throws Exception {
        
        try {
            serviceFactory.setAuthContext(authFactory.buildContext(headers));
            
            RoomsDao roomsDao = new RoomsDao(null);
            return roomsDao.getCriteria(orgdomain);
        } catch (AuthContextException e) {
            // TODO Use OAuth2 for real.
            e.printStackTrace();
            throw new WebApplicationException(buildBearerChallenge(e).build());
        } catch (Exception ex) {
            ApiLogger.logger.log(Level.SEVERE, "Error getting search criteria database", ex);
            throw ex;
        }
    }

    /**
     * method for GET /{organization}/roomlists/{roomlist}/rooms
     * <p>
     * Example URL:
     * http://localhost:8080/pfizer.com/roomlists/{roomlist}/rooms?start=2017-05-25T00:00:00Z&end=2017-05-31T23:59:59Z
     * </p>
     * 
     * @param buildingEmail The email of the room list to search for
     * @return A list of rooms in a room list
     * @throws ServiceResponseException If the result is not 200 OK.
     * @throws Exception On an unexpected error.
     */
    @GET
    @Path("/{orgdomain}/roomlists/{roomlist}/rooms")
    @Produces("application/json")
    public List<Room> getBuildingRooms(@Context HttpHeaders headers,
                    @PathParam("orgdomain") String orgdomain,
                    @PathParam("roomlist") String buildingEmail,
                    @DefaultValue("") @QueryParam("start") String start,
                    @DefaultValue("") @QueryParam("end") String end) throws Exception {
        
        try {
            serviceFactory.setAuthContext(authFactory.buildContext(headers));
            
            try (ExchangeService service = serviceFactory.create()) {
    
                RoomsDao roomsDao = new RoomsDao(service);
                return roomsDao.getBuildingRooms(buildingEmail,start,end);
            } catch (Exception e) {
                ApiLogger.logger.log(Level.SEVERE, "Error getting rooms for a building", e);
                throw e;
            }
        } catch (AuthContextException e) {
            // TODO Use OAuth2 for real.
            e.printStackTrace();
            throw new WebApplicationException(buildBearerChallenge(e).build());
        }
    }


    /**
     * Builds a WWW-Authenticate response for an oauth 2 Bearer token.
     * http://stackoverflow.com/questions/8341763/proper-www-authenticate-header-for-oauth-provider
     * https://tools.ietf.org/html/rfc6750#section-3
     * 
     * @param authException The AuthenticationException
     * @return A ResponseBuilder with WWW-Authenticate Bearer header.
     */
    private ResponseBuilder buildBearerChallenge(AuthContextException authException) {
        ResponseBuilder builder = Response.status(Status.UNAUTHORIZED).type(MediaType.TEXT_PLAIN)
                        .header(WWW_AUTHENTICATE, BEARER_CHALLENGE)
                        //.header(WWW_AUTHENTICATE, BASIC_CHALLENGE)
                        .entity(authException.toString());
        return builder;
    }
}
