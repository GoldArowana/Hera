package com.aries.hera.client.thrift.exception;

public class CallFailedException extends Exception {
    public CallFailedException() {
        super();
    }

    public CallFailedException(String message) {
        super(message);
    }

    public CallFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
