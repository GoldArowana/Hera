package com.aries.hera.client.thrift.function;

import com.aries.hera.client.thrift.exception.ThriftRuntimeException;

import java.util.Objects;
import java.util.function.Function;

public class Try {

    public static <T, R> Function<T, R> of(UncheckedFunction<T, R> clientFunction) {
        Objects.requireNonNull(clientFunction);
        return client -> {
            try {
                return clientFunction.invoke(client);
            } catch (Exception ex) {
                throw new ThriftRuntimeException("调用此服务失败:" + client.getClass().getEnclosingClass().getSimpleName(), ex);
            }
        };
    }

    @FunctionalInterface
    public interface UncheckedFunction<T, R> {
        R invoke(T t) throws Exception;
    }
}