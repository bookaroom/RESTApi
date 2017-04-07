package com.comeet;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "amenity")
public class Amenity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected static final String NAME = "name";
    protected static final String DESCRIPTION = "description";
    
    private String name = "";
    private String description = "";
    
    public String getName() {
        return name;
    }

    @XmlElement(name = NAME)
    public void setName(String name) { 
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }

    @XmlElement(name = DESCRIPTION)
    public void setDescription(String description) { 
        this.description = description;
    }
    

    
}
