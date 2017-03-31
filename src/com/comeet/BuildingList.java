package com.comeet;

import java.util.ArrayList;
import java.util.List;

public class BuildingList {
    private String email;
    private String metroArea;
    private List<String> roomlists;
    
    public BuildingList() {
        roomlists = new ArrayList();
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void addBuilding(String buildingName) {
        this.roomlists.add(buildingName);
    }
    
    public void setMetro(String metroArea) {
        this.metroArea = metroArea;
    }
    
    public String getEmail() {
        return email;
    }
    
    public List<String>  getBuildings() {
        return roomlists; 
    }
    
    public String getMetro() {
        return metroArea;
    }
}
