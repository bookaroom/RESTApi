package com.comeet;  

import java.io.Serializable;  
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
   private String latitude = "";
   private String longitude = "";
   private Integer capacity = -1;

   
   
   public Room(){} 

   
 
   public String getName() { 
	      return name; 
   }
   
   @XmlElement 
   public void setName(String name) { 
	   this.name = name;
   } 
	   
   public String getEmail() { 
	      return email; 
   }

   @XmlElement 
   public void setEmail(String email) { 
	   this.email = email;
   } 

   public String getAddress() { 
	      return address; 
   }

   @XmlElement 
   public void setAddress(String email) { 
	   this.address = address;
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
   
   
   
  
   
  
} 