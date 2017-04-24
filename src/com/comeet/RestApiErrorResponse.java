package com.comeet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Root element of json error response.
 * Follows this style guide: https://google.github.io/styleguide/jsoncstyleguide.xml#error
 */
@XmlRootElement
public class RestApiErrorResponse {

    private static final String apiVersion = "0.17.0423";
    
    private RestApiError error;

    public RestApiErrorResponse(RestApiError apiError) {
        this.error = apiError;
    }

    @XmlElement
    public String getApiVersion() {
        return apiVersion;
    }

    @XmlElement
    public RestApiError getError() {
        return error;
    }

    public void setError(RestApiError error) {
        this.error = error;
    }    
}
