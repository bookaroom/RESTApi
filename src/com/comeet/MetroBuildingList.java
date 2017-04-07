package com.comeet;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to store the search criteria for a user. This class represents a metro area
 * and it's buildings
 */
public class MetroBuildingList {
    
    private String metroArea;
    private Map<String, String> roomlists;
    
    /**
     * Initialize class, setting the roomlist array.
     */
    public MetroBuildingList() {
        roomlists = new HashMap<>();
    }
    
    /**
     * Set the email of the roomlist.
     * @param email roomlist email 
     * @param name roomlist name 
     */
    public void setRoomlist(String email, String name) {
        this.roomlists.put(email, name);
    }
    
    /**
     * Set the metro area of the buildings. 
     * @param metroArea  metro area name
     */    
    public void setMetro(String metroArea) {
        this.metroArea = metroArea;
    }
    
    /**
     * Get email-building name Map of the metro area.
     * @return  Map of buildings and email addresses in the metro area  
     */
    public Map<String,String>  getRoomlists() {
        return roomlists; 
    }
    
    /**
     * Get the metro area name.
     * @return  name of the metro area  
     */
    public String getMetro() {
        return metroArea;
    }
}
