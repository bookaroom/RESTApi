package com.comeet;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

import net.sf.json.JSONObject;


public class testRooms {
    
    private final String USER_AGENT = "Mozilla/5.0";
    @Test
    public void testTest() {
        
        assertEquals(1,1);
    }
    
    @Test 
    public void test_getRooms() {
        
 
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
                
                System.out.println("Response Code : " + responseCode);
                BufferedReader in = null;
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
     
                String inputLine;
                StringBuffer response = new StringBuffer();

     
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

      
                in.close();
               
                

            //print result
            
            System.out.println(response.toString());
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            assertEquals(200, responseCode);


        }

    


}
