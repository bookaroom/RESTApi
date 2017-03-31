package com.comeet.exchange;

import com.comeet.auth.AuthContext;
import com.comeet.utilities.ApiLogger;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.TokenCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;

public class ExchangeServiceFactoryImpl implements ExchangeServiceFactory {

    /**
     * O365 Exchange Web Services endpoint string documented at
     * https://msdn.microsoft.com/en-us/library/office/dn789003(v=exchg.150).aspx Note: Implement
     * autodiscover for Exchange 2013 compatibility
     * https://msdn.microsoft.com/en-us/library/office/jj900169(v=exchg.150).aspx
     */
    protected static final String EWS_ENDPOINT_STRING =
                    "https://outlook.office365.com/EWS/Exchange.asmx";

    private static final int LOGGED_TOKEN_LENGTH = 8;
    
    private static final String MASKED_PASSWORD_CHAR = "*";

    private AuthContext authContext = null;

    @Override
    public void setAuthContext(AuthContext context) {
        this.authContext = context;
    }

    /**
     * Creates a new ExchangeService connection.
     * 
     * @return new ExchangeService.
     * @throws ExchangeClientException When connection initialization fails.
     */
    @Override
    public ExchangeService create() throws ExchangeClientException {
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);

        ExchangeCredentials credentials = getCredentials();
        service.setCredentials(credentials);

        URI exchangeEndpoint = getExchangeWebServiceEndpoint();
        service.setUrl(exchangeEndpoint);

        return service;
    }

    private ExchangeCredentials getCredentials() throws ExchangeClientException {

        if (authContext == null) {
            ApiLogger.logger.log(Level.SEVERE, "Error getting credentials: authContext was null!");
            throw new ExchangeClientException("Null AuthContext");
        }

        ExchangeCredentials creds = null;
        
        // Note: Some work could be done to support other authentication types.
        String token = authContext.getBearerToken();
        if (token != null && token.length() > 0) {
            String shortToken = token.substring(0, token.length() < LOGGED_TOKEN_LENGTH ? token.length()
                            : LOGGED_TOKEN_LENGTH);
            ApiLogger.logger.log(Level.FINE,
                            String.format("Using token-based credentials [{0}...]", shortToken));
            try {
                return new TokenCredentials(token);
            } catch (Exception e) {
                ApiLogger.logger.log(Level.SEVERE, "Error building exchange token-based credentials",
                                e);
                throw new ExchangeClientException(e);
            }
        }
        
        String basicEncoded = authContext.getBasicEncoded();
        if (basicEncoded != null && basicEncoded.length() > 0) {
            String username = authContext.getBasicUsername();
            String password = authContext.getBasicPassword();
            StringBuilder maskedPasswordBuilder = new StringBuilder();
            for (int i = 0; i < password.length(); i++) {
                maskedPasswordBuilder.append(MASKED_PASSWORD_CHAR);
            }
            ApiLogger.logger.log(Level.FINE,
                            String.format("Using basic credentials [{0}:{1}]", username, maskedPasswordBuilder.toString()));
            return new WebCredentials(username, password);
        }
        
        throw new ExchangeClientException("No recognized authentication context provided.");    
    }

    private URI getExchangeWebServiceEndpoint() throws ExchangeClientException {
        try {
            return new URI(EWS_ENDPOINT_STRING);
        } catch (URISyntaxException e) {
            // EWS_ENDPOINT_STRING is a well-formed static final. Guaranteed not to throw if there's
            // not a code error.
            assert e == null;
            ApiLogger.logger.log(Level.SEVERE, "Error determining exchange web service endpoint",
                            e);
            throw new ExchangeClientException(e);
        }
    }


}
