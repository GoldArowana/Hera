package com.aries.hera.client.thrift;

import com.aries.com.aries.hera.contract.thrift.dto.ServiceInfo;
import com.aries.hera.client.thrift.exception.CallFailedException;
import org.apache.thrift.transport.TTransportException;

public class Main {

    public static void main(String[] args) {

//        for (int i = 0; i < 100; i++) {

        try {
            System.out.println(DiscoverClient.ping());

            System.out.println(DiscoverClient.getServices("lolo"));

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