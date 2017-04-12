package com.comeet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "room")
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected static final String NAME = "name";
    protected static final String EMAIL = "email";
    protected static final String ADDRESS = "address";
    protected static final String COUNTRY = "country";
    protected static final String STATE = "state";
    protected static final String METRO = "metro";
    protected static final String LATITUDE = "latitude";
    protected static final String LONGITUDE = "longitude";
    protected static final String CAPACITY = "capacity";
    protected static final String PICTURE = "picture";
    protected static final String NAVIGATION = "navigation";
    protected static final String AMENITIES = "amenities";
    protected static final String FREEBUSY = "freebusy";
    
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
    private List<Amenity> amenities;

    public Room() {
        freebusy = new ArrayList<>();
        amenities = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    @XmlElement(name = NAME)
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
     * Smtp email address.
     */
    @XmlElement(name = EMAIL)
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
    @XmlElement(name = ADDRESS)
    public void setAddress(String email) {
        this.address = email;
    }

    public String getCountry() {
        return country;
    }

    @XmlElement(name = COUNTRY)
    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    @XmlElement(name = STATE)
    public void setState(String state) {
        this.state = state;
    }

    public String getMetro() {
        return metroarea;
    }

    @XmlElement(name = METRO)
    public void setMetroarea(String metroarea) {
        this.metroarea = metroarea;
    }

    public String getLatitude() {
        return latitude;
    }

    @XmlElement(name = LATITUDE)
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @XmlElement(name = LONGITUDE)
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setNavigationMap(String url) {
        this.navigation = url;
    }

    @XmlElement(name = NAVIGATION)
    public String getNavigation() {
        return this.navigation;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @XmlElement(name = CAPACITY)
    public Integer getCapacity() {
        return this.capacity;
    }

    public void setRoomPic(String picture) {
        this.roomPicture = picture;
    }

    @XmlElement(name = PICTURE)
    public String getPicture() {
        return this.roomPicture;
    }

    public void addFreeBusyTime(FreeBusySlot slot) {
        this.freebusy.add(slot);
    }

    @XmlElement(name = FREEBUSY)
    public List<FreeBusySlot> getFreebusy() {
        return this.freebusy;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }
    
    @XmlElement(name = AMENITIES)
    public List<Amenity> getAmenities() {
        return this.amenities;
    }

    
}


