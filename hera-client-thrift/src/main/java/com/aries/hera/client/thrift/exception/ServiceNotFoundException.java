package com.aries.hera.client.thrift.exception;

public class ServiceNotFoundException extends Exception {
    public ServiceNotFoundException(String message) {
        super(message);
    }

    public ServiceNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
