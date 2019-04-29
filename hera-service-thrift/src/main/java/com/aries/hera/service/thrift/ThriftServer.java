package com.aries.hera.service.thrift;

import com.aries.hera.contract.thrift.service.DiscoverService;
import com.aries.hera.core.DiscoverUtils;
import com.aries.hera.core.pojo.ServicePojo;
import com.aries.hera.core.utils.PropertiesProxy;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;


@Slf4j
public class ThriftServer {

    public static void main(String[] args) {
        try {


            TMultiplexedProcessor processor = new TMultiplexedProcessor();

            { // 准备注册 DepartmentService
                DiscoverService.Iface discoverService = new DiscoverServiceImpl();
                DiscoverService.Processor discoverProcessor = new DiscoverService.Processor(discoverService);
                String simpleName = DiscoverService.class.getSimpleName();
                processor.registerProcessor(simpleName, discoverProcessor);
                log.info("simpleName:{}", simpleName);
            }

            PropertiesProxy propertiesProxy = new PropertiesProxy("hera-service.properties");
            String host = propertiesProxy.readProperty("host");
            int port = Integer.parseInt(propertiesProxy.readProperty("port"));

            new Thread(() -> {
                try {
                    TServerTransport serverTransport = new TServerSocket(port);
                    TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));
                    log.info("服务启动,端口:{}", port);
                    server.serve();

                } catch (Exception e) {
                    log.error("服务异常,error:{}", e.getMessage(), e);
                }
            }, "thrift-service-starter-thread").start();

            DiscoverUtils.registe(ServicePojo.builder().name("hera").host(host).port(port).build());

        } catch (Exception x) {
            log.error("创建服务失败,error:{}", x.getMessage(), x);
        }
    }
}