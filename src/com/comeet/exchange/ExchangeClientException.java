package com.comeet.exchange;

public class ExchangeClientException extends Exception {
    public ExchangeClientException(Exception innerException) {
        super(innerException);
    }
}
