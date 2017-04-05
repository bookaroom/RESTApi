package com.comeet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "room")
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name = "";
    private String email = "";
    private String address = "";
    private String country = "USA";
    private String state = "";
    private String metroarea = "";
    private String building = "";
    private String latitude = "";
    private String longitude = "";
    private Integer capacity = -1;
    private String navigation = "";
    private String roomPicture = "";
    private List<FreeBusySlot> freebusy;

    public Room() {
        freebusy = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Smtp email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Physical address.
     */
    @XmlElement
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Physical address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Physical address.
     */
    @XmlElement
    public void setAddress(String email) {
        this.address = email;
    }

    public String getCountry() {
        return country;
    }

    @XmlElement
    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    @XmlElement
    public void setState(String state) {
        this.state = state;
    }

    public String getMetroarea() {
        return metroarea;
    }

    @XmlElement
    public void setMetroarea(String metroarea) {
        this.metroarea = metroarea;
    }

    public String getLatitude() {
        return latitude;
    }

    @XmlElement
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @XmlElement
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setNavigationMap(String url) {
        this.navigation = url;
    }

    @XmlElement
    public String getNavigationMap() {
        return this.navigation;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    @XmlElement
    public String getBuilding() {
        return this.building;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @XmlElement
    public Integer getCapacity() {
        return this.capacity;
    }

    public void setRoomPic(String picture) {
        this.roomPicture = picture;
    }

    @XmlElement
    public String getRoomPic() {
        return this.roomPicture;
    }

    public void addFreeBusyTime(FreeBusySlot slot) {
        this.freebusy.add(slot);
    }
    
    @XmlElement
    public List<FreeBusySlot> getFreeBusy() {
        return this.freebusy;
    }

    
}


