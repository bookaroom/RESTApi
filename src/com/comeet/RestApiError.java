package com.comeet;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/** Api Error
 * Follows this style guide: https://google.github.io/styleguide/jsoncstyleguide.xml#error
 */
@XmlRootElement(name = "error")
public class RestApiError {

    public static final String HEADER_KEY = "X-com.comeet.RestApiError";
    
    private String message;
    private int code;
    private List<RestApiErrorDetail> errors;
    
    // This could be a much richer object.
    // private RestApiError cause;
    // private String traceId;

    /**
     * Build a RestApiError.
     * @param message The message.
     * @param statusCode The Http Status code.
     */
    public RestApiError(String message, int statusCode) {
        this.message = message;
        this.code = statusCode;
        this.errors = null;
    }

    @XmlElement
    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Http status code.
     */
    @XmlElement
    public void setCode(int statusCode) {
        this.code = statusCode;
    }

    /**
     * Http status code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Detailed errors.
     */
    public synchronized List<RestApiErrorDetail> getErrors() {
        if (errors == null) {
            errors = new ArrayList<RestApiErrorDetail>();
        }
        return errors;
    }

    /**
     * Detailed errors.
     */
    public void setErrors(List<RestApiErrorDetail> errors) {
        this.errors = errors;
    }
}
