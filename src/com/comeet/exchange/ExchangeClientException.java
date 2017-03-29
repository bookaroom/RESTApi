package com.comeet.exchange;

public class ExchangeClientException extends Exception {

    private static final long serialVersionUID = -5795606873997455734L;

    public ExchangeClientException(Exception innerException) {
        super(innerException);
    }

    public ExchangeClientException(String message) {
        super(message);
    }
}
