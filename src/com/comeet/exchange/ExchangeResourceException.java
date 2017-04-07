package com.comeet.exchange;

public class ExchangeResourceException extends Exception {

    private static final long serialVersionUID = -5795606873997455734L;

    public ExchangeResourceException(Exception cause) {
        super(cause);
    }

    public ExchangeResourceException(String message) {
        super(message);
    }

    public ExchangeResourceException(String message, Exception cause) {
        super(message, cause);
    }
    
}
