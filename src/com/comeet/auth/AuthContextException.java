package com.comeet.auth;

public class AuthContextException extends Exception {

    private static final long serialVersionUID = -4370663356018253768L;

    public AuthContextException(String message) {
        super(message);
    }

    public AuthContextException(String message, Exception e) {
        super(message, e);
    }

    public AuthContextException(Exception e) {
        super(e);
    }
}
