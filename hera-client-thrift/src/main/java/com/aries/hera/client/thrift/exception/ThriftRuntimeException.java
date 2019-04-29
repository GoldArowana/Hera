package com.aries.hera.client.thrift.exception;

public class ThriftRuntimeException extends RuntimeException {
    public ThriftRuntimeException() {
        super();
    }

    public ThriftRuntimeException(String message) {
        super(message);
    }

    public ThriftRuntimeException(String message, Throwable e) {
        super(message, e);
    }
}
