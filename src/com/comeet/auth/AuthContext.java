package com.comeet.auth;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class AuthContext {

    private static final String BASIC_AUTH_SEPARATOR = ":";
    
    private String bearerToken;

    private String basicDecoded;
    
    Encoder basicAuthEncoder = Base64.getMimeEncoder();
    Decoder basicAuthDecoder = Base64.getMimeDecoder();

    public AuthContext() {
        
    }

    /**
     * Gets the bearer token.
     * @return Bearer token as base64 string.
     */
    public String getBearerToken() {
        return this.bearerToken;
    }
    
    /**
     * Gets the bearer token.
     * @param token The Bearer token.
     */
    void setBearerToken(String token) {
        this.bearerToken = token;
    }

    /**
     * Gets the username portion of basic authentication credentials
     * @return username.
     */
    public String getBasicUsername() {
        int separatorIndex = basicDecoded.indexOf(BASIC_AUTH_SEPARATOR);
        return basicDecoded.substring(0, separatorIndex);
    }

    /**
     * Gets the password portion of basic authentication credentials
     * @return plaintext password.
     */
    public String getBasicPassword() {
        int separatorIndex = basicDecoded.indexOf(BASIC_AUTH_SEPARATOR);
        return basicDecoded.substring(separatorIndex + 1);
    }

    /**
     * Gets the base64 encoded basic authentication credentials
     * @return base64 string.
     */
    public String getBasicEncoded() {
        byte[] encodedBytes = basicAuthEncoder.encode(basicDecoded.getBytes());
        return new String(encodedBytes);
    }
        
    /**
     * Gets the decoded basic authentication credential pair.
     * @return Colon-delimited string.
     */
    public String getBasicDecoded() {
        return basicDecoded;
    }
    
    /**
     * Sets the decoded basic authentication credential pair.
     * @param decoded Colon-delimited string.
     */
    void setBasicDecoded(String decoded) {
        this.basicDecoded = decoded;
    }
    
}
