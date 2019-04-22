package com.aries.hera.client.thrift;

import com.aries.com.aries.hera.contract.thrift.dto.ServiceInfo;
import com.aries.com.aries.hera.contract.thrift.service.DiscoverService;
import com.aries.hera.client.thrift.exception.CallFailedException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class DiscoverClient {
    private static final AtomicBoolean shutdownHookFlag = new AtomicBoolean(false);

    private static final Logger log = LoggerFactory.getLogger(DiscoverClient.class);

    private static final Set<ServiceInfo> needShutdownServices = new HashSet<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                shutAllDown();
            } catch (TTransportException e) {
                log.error("shut all down error!!! {}", e.getMessage(), e);
            }
        }));
    }

    public static String ping() throws TTransportException, CallFailedException {
        DiscoverService.Client client = HeraClient.getSingleClient();
        try {
            return client.ping();
        } catch (TException e) {
            throw new CallFailedException("thrift方式调用ping()方法异常", e);
        }
    }

    public static List<ServiceInfo> getServices(String serviceName) throws TTransportException, CallFailedException {
        DiscoverService.Client client = HeraClient.getSingleClient();
        List<ServiceInfo> loloServiceList = null;
        try {
            loloServiceList = client.getServiceList(serviceName);
        } catch (TException e) {
            throw new CallFailedException("thrift方式调用getServiceList()方法异常", e);
        }
        return loloServiceList;
    }

    public static short registe(ServiceInfo serviceInfo) throws TTransportException, CallFailedException {
        DiscoverService.Client client = HeraClient.getSingleClient();
        ServiceInfo serviceInfoCopied = new ServiceInfo(serviceInfo);
        try {
            short state = client.registe(serviceInfoCopied);
            if (state == 1 || state == 0) {
                needShutdownServices.add(serviceInfo);
            }
            return state;
        } catch (TException e) {
            throw new CallFailedException("thrift方式调用getServiceList()方法异常", e);
        }
    }

    /**
     * jvm关闭时，会执行本方法里的线程
     */
    private static void startShutdownHoot(ServiceInfo serviceInfo) throws TTransportException {
        DiscoverService.Client client = HeraClient.getSingleClient();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                client.cancel(serviceInfo);
                log.info("注销服务成功：name" + serviceInfo.getName()
                        + ", host:" + serviceInfo.getHost()
                        + ", port:" + serviceInfo.getPort());
            } catch (TException e) {
                log.error("注销服务失败：name" + serviceInfo.getName()
                        + ", host:" + serviceInfo.getHost()
                        + ", port:" + serviceInfo.getPort(), e);
            }
        }));

    }

    public static void shutAllDown() throws TTransportException {
        DiscoverService.Client client = HeraClient.getSingleClient();
        for (ServiceInfo serviceInfo : needShutdownServices) {
            try {
                client.cancel(serviceInfo);
                log.info("注销服务成功：name" + serviceInfo.getName()
                        + ", host:" + serviceInfo.getHost()
                        + ", port:" + serviceInfo.getPort());
            } catch (TException e) {
                log.error("注销服务失败：name" + serviceInfo.getName()
                        + ", host:" + serviceInfo.getHost()
                        + ", port:" + serviceInfo.getPort(), e);
            }
        }
        HeraClient.close();
    }
}
