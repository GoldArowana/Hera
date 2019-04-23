package com.aries.hera.client.thrift;

import com.aries.hera.client.thrift.exception.CallFailedException;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

//        for (int i = 0; i < 100; i++) {

        try {
            log.info(DiscoverClient.ping());
            log.info(DiscoverClient.getServices("lolo").toString());

//            ServiceInfo serviceInfo = new ServiceInfo();
//            serviceInfo.setName("lolo").setHost("222.222.222.3").setPort(7878);
//            System.out.println(DiscoverClient.registe(serviceInfo));
//
//            ServiceInfo serviceInfo2 = new ServiceInfo();
//            serviceInfo2.setName("lolo").setHost("1.1.1.1").setPort(7878);
//            System.out.println(DiscoverClient.registe(serviceInfo2));
//
//            System.out.println(DiscoverClient.getServices("lolo"));

        } catch (TTransportException | CallFailedException e) {
            e.printStackTrace();
        }
//        }

    }
}