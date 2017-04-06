package com.comeet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import static org.junit.Assert.*;

public class testUserService {

    private final String USER_AGENT = "Mozilla/5.0";
    int responseCode = 0;
    List<Meeting> meetings = new ArrayList<Meeting>();
    
    public void getMeetingsInfo() {
        
        
        String url = "http://localhost:8080/JavaApplication/user/meetings?start=2017-03-25T22:00:00-0400&end=2017-03-30T12:00:00-0400";
        System.out.println("\nSending 'GET' request to URL : " + url);
        
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
            String modified = "{ userMeetings: " + response.toString() + "}";
            System.out.println(modified);             
            
            JSONObject ob = new JSONObject(modified);
            JSONArray data = ob.getJSONArray("userMeetings");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T' HH:mm:ssZ");
            List<Meeting> meetingsArray = new ArrayList<Meeting>();
            for(int i = 0; i < data.length(); i++)
            {
            
                Meeting m= new Meeting();
                m.setSubject((String) data.getJSONObject(i).get("subject"));
                //m.setStart( formatter.parse((String) data.getJSONObject(i).get("start")));
                //m.setEnd(formatter.parse((String) data.getJSONObject(i).get("end")));
                //m.setLocation((String) data.getJSONObject(i).get("location"));
                //m.setBody((String) data.getJSONObject(i).get("body"));
                
                meetingsArray.add(m);
            }
          
           meetings = meetingsArray;
            

            
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        

    }
    
    @org.junit.Test
    public void testSstatusResponseMeetings(){
        getMeetingsInfo();
       assertEquals(200, responseCode);
    }
    

}
