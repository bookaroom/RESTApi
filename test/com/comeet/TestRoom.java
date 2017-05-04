package com.comeet;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestRoom {

    private Room room = new Room();
    
    
    @Test
    public void testGetName(){
        room.setName("MyRoom");
        assertEquals("MyRoom", room.getName());  
    }
    
    @Test
    public void testGetEmail(){
        room.setEmail("me@meetl.ink");
        assertEquals("me@meetl.ink", room.getEmail());  
    }  
    
    
    @Test
    public void testGetAddress(){
        room.setAddress("My Address");
        assertEquals("My Address", room.getAddress());  
    }
    
    @Test
    public void testGetCountry(){
        room.setCountry("US");
        assertEquals("US", room.getCountry());  
    }
    
    @Test
    public void testGetState(){
        room.setState("MA");
        assertEquals("MA", room.getState());  
    } 
    
    
    @Test
    public void testGetMetro(){
        room.setMetroarea("Boston");
        assertEquals("Boston", room.getMetro());  
    }
    
    @Test
    public void testGetLatitude(){
        room.setLatitude("Latitude");
        assertEquals("Latitude", room.getLatitude());  
    }
    
    @Test
    public void testGetLongitude(){
        room.setLongitude("Longitude");
        assertEquals("Longitude", room.getLongitude());  
    }
    
    @Test
    public void testGetNavigation(){
        room.setNavigationMap("Navigation");
        assertEquals("Navigation", room.getNavigation());  
    }
    
    @Test
    public void testGetCapacity(){
        room.setCapacity(3);
        assertEquals((long)3, (long)room.getCapacity());  
    }
    
    @Test
    public void testGetRoomPic(){
        room.setRoomPic("Picture");
        assertEquals("Picture", room.getPicture());  
    }
    
    
    @Test
    public void testAddFreeBusySlot(){
        FreeBusySlot slot= new FreeBusySlot("1", "2", "3");
        room.addFreeBusyTime(slot);
        List<FreeBusySlot> list = new ArrayList<FreeBusySlot>();
        list.add(slot);
        assertEquals(list, room.getFreebusy());  
    } 
    
    @Test
    public void testGetAmenities(){
        Amenity amenity =new Amenity();
        
        List<Amenity> list = new ArrayList<Amenity>();
        list.add(amenity);
        room.addAmenities(list);
        assertEquals(list, room.getAmenities());  
    }
}