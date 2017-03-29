package com.comeet.exchange;

import com.comeet.auth.AuthContext;

import microsoft.exchange.webservices.data.core.ExchangeService;

/**
 * Factory for ExchangeService acquisition.
 */
public interface ExchangeServiceFactory {
    
    /**
     * Create an ExchanceService instance.
     * @return New ExchangeService.
     * @throws ExchangeClientException
     *      When there is a failure on the client (comeet) side.
     */
    public ExchangeService create() throws ExchangeClientException;

    /**
     * Sets the authorization context for the factory.
     * @param authContext The Authorization context.
     */
    public void setAuthContext(AuthContext authContext);

}
