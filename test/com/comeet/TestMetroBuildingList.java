package com.comeet;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class TestMetroBuildingList {

    private MetroBuildingList metroBuilding = new MetroBuildingList();
    private Map<String, String> roomlists;
    
    @Test
    public void testGetRoomLists(){
        metroBuilding.setRoomlist("email@meetl.ink", "Boston");
        assertEquals(roomlists, metroBuilding.getRoomlists());
    }
    
    @Test
    public void testGetMetroArea(){
        metroBuilding.setMetro("Boston");
        assertEquals("Boston", metroBuilding.getMetro());
        
    }
}
