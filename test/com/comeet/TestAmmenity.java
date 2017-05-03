package com.comeet;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestAmmenity {
    Amenity amenity = new Amenity();
    
    @Test
    public void testGetName(){
        amenity.setName("Flat Screen");
        assertEquals("Flat Screen",amenity.getName());
    }
    
    @Test
    public void testGetDescription(){
        amenity.setDescription("72 inches");
        assertEquals("72 inches", amenity.getDescription());
    }

}
