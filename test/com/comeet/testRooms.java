package com.comeet;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;





public class testRooms {
    
    private final String USER_AGENT = "Mozilla/5.0";
    private int statusCode = 0;
    private List<Room> rooms = new ArrayList<Room>();
   

    
    public void getRoomsMethodInfo() {
        
 
            String url = "http://localhost:8080/JavaApplication/rooms";
            System.out.println("\nSending 'GET' request to URL : " + url);
            int responseCode = 0;
            try{
                URL obj = null;
                obj = new URL(url);
                HttpURLConnection con = null;
                con = (HttpURLConnection) obj.openConnection();
                // optional default is GET
                con.setRequestMethod("GET");
                //add request header
                con.setRequestProperty("User-Agent", USER_AGENT);
                      
                responseCode = con.getResponseCode();
                statusCode = responseCode;
                
                System.out.println("Response Code : " + responseCode);
                BufferedReader in = null;
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
     
                String inputLine;
                StringBuffer response = new StringBuffer();

     
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

               //
                System.out.println(response);
                String modified = "{ rooms: " + response.toString() + "}";
                System.out.println(modified);             
                
                JSONObject ob = new JSONObject(modified);
                JSONArray data = ob.getJSONArray("rooms");
                List<Room> roomsArray = new ArrayList<Room>();
                for(int i = 0; i < data.length(); i++)
                {
                
                    Room r= new Room();
                    r.setName((String) data.getJSONObject(i).get("name"));
                    r.setEmail((String) data.getJSONObject(i).get("email"));
                    r.setAddress((String) data.getJSONObject(i).get("address"));
                    r.setCountry((String) data.getJSONObject(i).get("country"));
                    r.setState((String) data.getJSONObject(i).get("state"));
                    r.setMetroarea((String) data.getJSONObject(i).get("metroarea"));
                    r.setLatitude((String) data.getJSONObject(i).get("latitude"));
                    r.setLongitude((String) data.getJSONObject(i).get("longitude"));
                    r.setNavigationMap((String) data.getJSONObject(i).get("navigationMap"));
                    r.setBuilding((String) data.getJSONObject(i).get("building"));
                    r.setCapacity((Integer) data.getJSONObject(i).get("capacity"));
                    r.setRoomPic((String) data.getJSONObject(i).get("roomPic"));
                    roomsArray.add(r);
                }
              
               rooms = roomsArray;
                

                
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            


        }

    @Test
    public void test_roomCallStatusCode(){
    
    getRoomsMethodInfo();
    assertEquals(200, statusCode);
}

    @Test
    public void test_roomsNotEmpty(){
        getRoomsMethodInfo();
               
       if (rooms.isEmpty()){
           assertFalse(true);
       }
        
       assertFalse(rooms.isEmpty());
    }
    
    
    @Test
    public void test_roomExists(){
        getRoomsMethodInfo();
               
        String email = "";
        for (Room room : rooms) {
            if(room.getName().equals("[GSD] [Loeb] Study Room A")){
                email = room.getEmail();
            }
            assertEquals("gsdloebStudyA@meetl.ink", email);
        }       
        
    }
    
    
    
}
