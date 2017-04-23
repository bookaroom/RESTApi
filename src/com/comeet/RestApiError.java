package com.comeet;

import java.io.UnsupportedEncodingException;

public class RestApiError {
    
    public static final String HEADER_KEY = "X-com.comeet.RestApiError";
    
    private String message;

    public RestApiError(String message) {
        this.message = message;
    }

    /**
     * Sets the message plaintext.
     * @param message The message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * The message in this string.
     * @return message.
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Gets the message in a format safe for transmission in an HTTP header.
     * @return Encoded string.
     */
    public String getEncodedMessage() {
        try {
            return java.net.URLEncoder.encode(message, java.nio.charset.StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            assert e == null;
            return "";
        }
    }
    
}
