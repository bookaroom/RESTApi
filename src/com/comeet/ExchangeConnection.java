package com.comeet;

import java.net.URI;
import java.net.URISyntaxException;

import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;


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
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.recurrence.pattern.Recurrence;
import microsoft.exchange.webservices.data.property.complex.time.TimeZoneDefinition;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;



public class ExchangeConnection {
	
	
	
	
	
	public ExchangeService getService() throws URISyntaxException{
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		ExchangeCredentials credentials = new WebCredentials("adminish@meetl.ink", "Springe599");
		service.setCredentials(credentials);
		service.setUrl(new URI("https://outlook.office365.com/EWS/Exchange.asmx"));
		
		return service;
		
	}

}
