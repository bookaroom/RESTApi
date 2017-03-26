package com.comeet;  

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET; 
import javax.ws.rs.Path; 
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceResponseException;  
@Path("") 

public class UserService {  
   
   
   
   
   @GET 
   @Path("/rooms") 
   @Produces("application/json")
   // @Produces(MediaType.APPLICATION_XML) 
   public List<Room> getRooms() { 
	   RoomsDao roomsDao = new RoomsDao(); //each call should define new instance of DAO object
	   return roomsDao.getAllRooms(); 

   }

// http://localhost:8080/JavaApplication/user/meetings?start=2017-03-25*12:00:00&end=2017-06-25*12:00:00
   @GET 
   @Path("/user/meetings") 
   @Produces("application/json")
   // @Produces(MediaType.APPLICATION_XML) 
   public List<Meeting> getUserMeetings(
		   @DefaultValue("") @QueryParam("start") String start,
	       @DefaultValue("") @QueryParam("end") String end
		   ) throws ServiceResponseException, Exception { 
	   
	   
	   
	   UsersDao ud = new UsersDao();

	   return ud.getUserMeetings(start, end);
   }
   
   
   
   //http://localhost:8080/JavaApplication/rooms/reserve?start=2017-03-10*9:00:00&end=2017-03-10*10:00:00&subject=testSubject&body=testBody&recipients=CambMa1Story305@meetl.ink,jablack@meetl.ink
   @GET 
   @Path("/rooms/reserve") 
   @Produces("application/json")
   // @Produces(MediaType.APPLICATION_XML) 
   public List<Meeting> getRoomsTwo(
		   @DefaultValue("") @QueryParam("start") String start,
	       @DefaultValue("") @QueryParam("end") String end,
	       @DefaultValue("") @QueryParam("subject") String subject,
	       @DefaultValue("") @QueryParam("body") String body,
	       @DefaultValue("") @QueryParam("recipients") String recipients
		   		) throws ServiceResponseException, Exception { 
	   
	   List<String> recips = Arrays.asList(recipients.split("\\s*,\\s*"));
	   
	   RoomsDao roomsDao = new RoomsDao(); //each call should define new instance of DAO object
	   return roomsDao.makeAppointment(start, end, subject, body, recips);

   }
   
   
}
   
   
   
   
/*   @GET 
   @Path("/meetings/userid") //userid is an email 
  @Produces("application/json")
  // @Produces(MediaType.APPLICATION_XML) 
   public List<Room> getUserMeetings() { 
	   MeetingsDao meetingsDao = new MeetingsDao();
	   return meetingsDao.getUserMeetings(String email); 

   }  */
