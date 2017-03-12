package com.comeet;  

import java.util.ArrayList;
import java.util.List; 
import javax.ws.rs.GET; 
import javax.ws.rs.Path; 
import javax.ws.rs.Produces; 
import javax.ws.rs.core.MediaType;  
@Path("/UserService") 

public class UserService {  
   
   RoomsDao roomsDao = new RoomsDao();
   
   
   @GET 
   @Path("/rooms") 
  @Produces("application/json")
  // @Produces(MediaType.APPLICATION_XML) 
   public List<Room> getRooms() { 

	   return roomsDao.getAllRooms(); 

   }  
}