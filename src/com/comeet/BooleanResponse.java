package com.comeet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


public class BooleanResponse implements Serializable {

    boolean success = true;
    
    
    public BooleanResponse(boolean input) {
        success = input;
    }
    
    
    
    public boolean getSuccess() {
        return success;
    }
    
    
    @XmlElement(name = "succcess")
    public void setSuccess(boolean input) {
        this.success = input;
    }
    
    
    
}