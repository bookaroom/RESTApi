package com.comeet;

import java.net.URI;
import java.net.URISyntaxException;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;


public class ExchangeConnection {

    /**
     * Gets the Exchange ews service.
     * 
     * @return Ews Service connection.
     * @throws URISyntaxException When the hardcoded uri is malformed.
     */
    public ExchangeService getService() throws URISyntaxException {
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
        ExchangeCredentials credentials = new WebCredentials("adminish@meetl.ink", "Springe599");
        service.setCredentials(credentials);
        service.setUrl(new URI("https://outlook.office365.com/EWS/Exchange.asmx"));

        return service;
    }

}
