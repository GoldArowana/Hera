package com.aries.hera.service.thrift;

import com.aries.com.aries.hera.contract.thrift.service.DiscoverService;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;


public class ThriftServer {

    public static DiscoverService.Iface service;

    public static DiscoverService.Processor processor;

    public static void main(String[] args) {
        try {
            service = new DiscoverServiceImpl();
            processor = new DiscoverService.Processor(service);

            new Thread(() -> {
                try {
                    TServerTransport serverTransport = new TServerSocket(8020);
                    TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));
                    System.out.println("Starting the simple server...");
                    server.serve();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "thrift-service-starter-thread").start();

        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}