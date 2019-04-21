package com.aries.hera.client.thrift;

import com.aries.com.aries.hera.contract.thrift.service.DiscoverService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class HeraClient {

    public static DiscoverService.Client getSingleClient() throws TTransportException {
        TTransport transport = TransportFactory.getSingleTransport();
        TProtocol protocol = new TBinaryProtocol(transport);
        DiscoverService.Client client = new DiscoverService.Client(protocol);
        return client;
    }

    public static void close() {
        TransportFactory.closeSingleTransport();
    }
}
