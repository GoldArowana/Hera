package com.aries.hera.client.thrift;

import com.aries.hera.client.thrift.exception.CallFailedException;
import com.aries.hera.client.thrift.exception.ServiceNotFoundException;
import com.aries.hera.contract.thrift.dto.ServiceInfo;
import com.aries.hera.contract.thrift.service.DiscoverService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiscoverClient {
    private static final Logger log = LoggerFactory.getLogger(DiscoverClient.class);

    private static final Set<ServiceInfo> needShutdownServices = new HashSet<>();

    static {
        // jvm关闭时会执行这里
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
        try {
            List<ServiceInfo> serviceList = client.getServiceList(serviceName);
            return CollectionUtils.isEmpty(serviceList) ? Collections.emptyList() : serviceList;
        } catch (TException e) {
            throw new CallFailedException("thrift方式调用getServiceList()方法异常", e);
        }
    }

    public static ServiceInfo getFirstService(String serviceName) throws TTransportException, CallFailedException, ServiceNotFoundException {
        List<ServiceInfo> services = getServices(serviceName);
        if (CollectionUtils.isEmpty(services)) {
            throw new ServiceNotFoundException("未找到服务: " + serviceName);
        }
        return services.get(0);
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
