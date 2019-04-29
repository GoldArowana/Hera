package com.aries.hera.client.thrift;

import com.aries.hera.contract.thrift.dto.ServiceInfo;
import com.aries.hera.core.utils.PropertiesProxy;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class TransportFactory {
    private static volatile TTransport transport;

    public static TTransport getSingleTransport() throws TTransportException {
        if (transport == null) {
            synchronized (TransportFactory.class) {
                if (transport == null) {
                    PropertiesProxy propertiesProxy = new PropertiesProxy("hera-service.properties");
                    String host = propertiesProxy.readProperty("host");
                    int port = Integer.parseInt(propertiesProxy.readProperty("port"));
                    transport = new TSocket(host, port);
                    System.out.println("open transport, host:" + host + ",port:" + port);
                    transport.open();
                }
            }
        }
        return transport;
    }

    public static TTransport getSingleTransport(ServiceInfo serviceInfo) throws TTransportException {
        if (transport == null) {
            synchronized (TransportFactory.class) {
                if (transport == null) {
                    transport = new TSocket(serviceInfo.getHost(), serviceInfo.getPort());
                    System.out.println("open transport, host:" + serviceInfo.getHost() + ",port:" + serviceInfo.getPort());
                    transport.open();
                }
            }
        }
        return transport;
    }

    public static void closeSingleTransport() {
        if (transport != null) {
            synchronized (TransportFactory.class) {
                if (transport != null) {
                    transport.close();
                }
            }
        }
    }
}
