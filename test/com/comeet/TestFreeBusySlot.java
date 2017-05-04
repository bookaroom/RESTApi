package com.comeet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestFreeBusySlot {
    FreeBusySlot busySlot = new FreeBusySlot("1", "2", "3");
    
    @Test
    public void testGetStartSlot(){
        assertEquals("1", busySlot.getStart());
    }
    
    @Test
    public void testSetStartSlot() {
        busySlot.setStart("Start");
        assertEquals("Start", busySlot.getStart());
    }
    
    @Test
    public void testSetEnd(){
        busySlot.setEnd("End");
        assertEquals("End", busySlot.getEnd());
    }
    
    @Test
    public void testSetStatus(){
        busySlot.setStatus("Status");
        assertEquals("Status", busySlot.getStatus());
    }
    
}