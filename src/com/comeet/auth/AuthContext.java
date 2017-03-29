package com.comeet.auth;

public class AuthContext {

    private String bearerToken;

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
    
}
