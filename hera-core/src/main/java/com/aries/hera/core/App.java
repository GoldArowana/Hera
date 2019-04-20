package com.aries.hera.core;

import com.aries.hera.core.pojo.ServicePojo;

import java.util.List;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {
        ServicePojo servicePojo = ServicePojo.builder()
                .name("hera")
                .ip("127.0.0.1")
                .port(9000)
                .build();
        List list = DiscoverUtils.discover("hera");
        System.out.println(list);
    }
}
