package com.aries.hera.service.thrift;

import com.aries.com.aries.hera.contract.thrift.service.DiscoverService;
import com.aries.hera.core.utils.PropertiesProxy;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;


@Slf4j
public class ThriftServer {

    private static DiscoverService.Iface service;

    private static DiscoverService.Processor processor;

    public static void main(String[] args) {
        try {
            service = new DiscoverServiceImpl();
            processor = new DiscoverService.Processor(service);

            new Thread(() -> {
                try {
                    PropertiesProxy propertiesProxy = new PropertiesProxy("hera-service.properties");
                    int port = Integer.parseInt(propertiesProxy.readProperty("port"));

                    TServerTransport serverTransport = new TServerSocket(port);
                    TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));
                    log.info("服务启动,端口:{}", port);
                    server.serve();

                } catch (Exception e) {
                    log.error("服务异常,error:{}", e.getMessage(), e);
                }
            }, "thrift-service-starter-thread").start();

        } catch (Exception x) {
            log.error("创建服务失败,error:{}", x.getMessage(), x);
        }
    }
}