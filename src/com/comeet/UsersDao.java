
package com.comeet;  



import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileNotFoundException;  
import java.io.FileOutputStream; 
import java.io.IOException; 
import java.io.ObjectInputStream; 
import java.io.ObjectOutputStream; 
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.comeet.data.*;

import microsoft.exchange.webservices.data.*;
import microsoft.exchange.webservices.data.autodiscover.IAutodiscoverRedirectionUrl;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;


import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceResponseException;
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.core.service.schema.AppointmentSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.misc.ITraceListener;
import microsoft.exchange.webservices.data.property.complex.Attendee;
import microsoft.exchange.webservices.data.property.complex.AttendeeCollection;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.recurrence.pattern.Recurrence;
import microsoft.exchange.webservices.data.property.complex.time.TimeZoneDefinition;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;



public class UsersDao { 
	private List<String> getApptAttendees(Appointment appt) throws ServiceLocalException{
		List<String> myAttendees = new ArrayList<String>();
		AttendeeCollection ac = appt.getOptionalAttendees();
		for(Attendee a: ac){
			myAttendees.add(a.getAddress());
		}
			   
		ac = appt.getRequiredAttendees();
		for(Attendee a: ac){
			myAttendees.add(a.getAddress());
				   
		}

		return myAttendees;
	}

	
	public List<Meeting> getUserMeetings(String start, String end) throws Exception{ 

		ExchangeService service = (new ExchangeConnection()).getService();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd*HH:mm:ss");
		Date startDate = formatter.parse(start);//"2010-05-01 12:00:00");
		Date endDate = formatter.parse(end); //"2010-05-30 13:00:00");
		CalendarFolder cf=CalendarFolder.bind(service, WellKnownFolderName.Calendar);
		FindItemsResults<Appointment> findResults = cf.findAppointments(new CalendarView(startDate, endDate));
		   
		List<Meeting> meetings = new ArrayList<Meeting>();
		   
		for (Appointment appt : findResults.getItems()) {
			if(appt != null){
				Meeting m = new Meeting();
				   
				m.setStartTime(appt.getStart());
				m.setDuration(appt.getDuration().MINUTES/1000);
				m.setTitle(appt.getSubject());
				m.setMessage("");//appt.getBody().toString());
				m.setLocation(appt.getLocation());
				   
				  
				m.setAttendees(getApptAttendees(appt));
				    
				meetings.add(m);
			}

		}		   
		   
		return meetings;
	} 
	
	
	
}