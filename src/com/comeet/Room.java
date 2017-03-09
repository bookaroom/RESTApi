package com.comeet;  

import java.io.Serializable;  
import javax.xml.bind.annotation.XmlElement; 
import javax.xml.bind.annotation.XmlRootElement; 
@XmlRootElement(name = "room") 

public class Room implements Serializable {  
   private static final long serialVersionUID = 1L; 
   private int id; 
   private String type; 
   private String name;  
   public Room(){} 
    
   public Room(int id, String type, String name){  
      this.id = id; 
      this.type = type; 
      this.name = name; 
   }  
   
   
   
   
   
   public int getId() { 
      return id; 
   }  
   @XmlElement 
   public void setId(int id) { 
      this.id = id; 
   } 
   
   
   
   
   
   
   public String getType() { 
      return type; 
   } 
   @XmlElement
   public void setType(String type) { 
      this.type = type; 
   } 
   
   
   
   
   
   
   
   public String getName() { 
      return name; 
   } 
   @XmlElement 
   public void setName(String name) { 
      this.name = name; 
   }   
} 