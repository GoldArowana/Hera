package com.aries.hera.client.thrift.exception;

public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException(String message) {
        super(message);
    }

    public ServiceNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
