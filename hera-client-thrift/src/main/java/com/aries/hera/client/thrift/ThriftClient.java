package com.aries.hera.client.thrift;

import com.aries.com.aries.hera.contract.thrift.dto.ServiceInfo;
import com.aries.hera.client.thrift.exception.CallFailedException;
import org.apache.thrift.transport.TTransportException;

public class ThriftClient {

    public static void main(String[] args) {
        try {
            System.out.println(DiscoverClient.ping());

            ServiceInfo serviceInfo = new ServiceInfo();
            serviceInfo.setName("lolo").setHost("2.2.2.3").setPort(7878);
            System.out.println(DiscoverClient.registe(serviceInfo));

            System.out.println(DiscoverClient.getServices("lolo"));

        } catch (TTransportException | CallFailedException e) {
            e.printStackTrace();
        }
    }
}