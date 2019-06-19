package com.aries.hera.service.thrift;

import com.aries.hera.contract.thrift.service.DiscoverService;
import com.aries.hera.core.utils.PropertiesProxy;
import com.aries.hera.service.thrift.impl.DiscoverServiceImpl;
import com.aries.hera.service.thrift.pojo.ServicePojo;
import com.aries.hera.service.thrift.worker.DiscoverWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import static com.aries.hera.core.constance.ConfConst.HERA_APP_NAME;
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

            // 启动thrift服务
            new Thread(() -> {
                try {
                    TServerTransport serverTransport = new TServerSocket(PORT);
                    TThreadPoolServer.Args poolArgs = new TThreadPoolServer.Args(serverTransport)
                            .processor(processor)
                            .maxWorkerThreads(16)
                            .minWorkerThreads(2)
                            .requestTimeout(5000);
                    TServer server = new TThreadPoolServer(poolArgs);
                    log.info("服务启动,端口:{}", PORT);
                    server.serve();

                } catch (Exception e) {
                    log.error("服务异常,error:{}", e.getMessage(), e);
                }
            }, "thrift-service-starter-thread").start();

            // 把自己注册进去
            {
                PropertiesProxy propertiesProxy = new PropertiesProxy("/opt/config/local.properties");
                String host = propertiesProxy.readProperty("host");
                ServicePojo thisHera = ServicePojo.builder().name(HERA_APP_NAME).host(host).port(PORT).build();
                DiscoverWorker.registe(thisHera);
                Runtime.getRuntime().addShutdownHook(new Thread(() -> DiscoverWorker.cancel(thisHera)));
            }
        } catch (Exception x) {
            log.error("创建服务失败,error:{}", x.getMessage(), x);
        }
    }
}