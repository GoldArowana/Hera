package com.aries.hera.service.thrift;

import com.aries.hera.contract.thrift.service.DiscoverService;
import com.aries.hera.core.utils.PropertiesProxy;
import com.aries.hera.service.thrift.impl.DiscoverServiceImpl;
import com.aries.hera.service.thrift.pojo.ServicePojo;
import com.aries.hera.service.thrift.worker.DiscoverWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import static com.aries.hera.core.constance.ConfConst.APP_NAME;
import static com.aries.hera.core.constance.ConfConst.PORT;


@Slf4j
public class ThriftServerStarter {

    public static void main(String[] args) {
        try {

            TMultiplexedProcessor processor = new TMultiplexedProcessor();

            { // 准备注册 DepartmentService
                DiscoverService.Iface discoverService = new DiscoverServiceImpl();
                DiscoverService.Processor discoverProcessor = new DiscoverService.Processor<>(discoverService);
                String simpleName = DiscoverService.class.getSimpleName();
                processor.registerProcessor(simpleName, discoverProcessor);
                log.info("simpleName:{}", simpleName);
            }

            PropertiesProxy propertiesProxy = new PropertiesProxy("/opt/config/local.properties");
            String host = propertiesProxy.readProperty("host");

            new Thread(() -> {
                try {
                    TServerTransport serverTransport = new TServerSocket(PORT);
                    TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));
                    log.info("服务启动,端口:{}", PORT);
                    server.serve();

                } catch (Exception e) {
                    log.error("服务异常,error:{}", e.getMessage(), e);
                }
            }, "thrift-service-starter-thread").start();

            ServicePojo thisHera = ServicePojo.builder().name(APP_NAME).host(host).port(PORT).build();

            DiscoverWorker.registe(thisHera);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> DiscoverWorker.cancel(thisHera)));
        } catch (Exception x) {
            log.error("创建服务失败,error:{}", x.getMessage(), x);
        }
    }
}