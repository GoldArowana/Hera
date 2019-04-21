package com.aries.hera.client.thrift;

import com.aries.com.aries.hera.contract.thrift.dto.ServiceInfo;
import com.aries.com.aries.hera.contract.thrift.service.DiscoverService;
import com.aries.hera.client.thrift.exception.CallFailedException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

import java.util.List;

public class DiscoverClient {
    public static String ping() throws TTransportException, CallFailedException {
        DiscoverService.Client client = ClientUtils.getSingleClient();
        try {
            return client.ping();
        } catch (TException e) {
            throw new CallFailedException("thrift方式调用ping()方法异常", e);
        }
    }

    public static List<ServiceInfo> getServices(String serviceName) throws TTransportException, CallFailedException {
        DiscoverService.Client client = ClientUtils.getSingleClient();
        List<ServiceInfo> loloServiceList = null;
        try {
            loloServiceList = client.getServiceList(serviceName);
        } catch (TException e) {
            throw new CallFailedException("thrift方式调用getServiceList()方法异常", e);
        }
        return loloServiceList;
    }

    public static short registe(ServiceInfo serviceInfo) throws TTransportException, CallFailedException {
        DiscoverService.Client client = ClientUtils.getSingleClient();
        try {
            short state = client.registe(serviceInfo);
            if (state == 1) {
                startShutdownHoot(serviceInfo);
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
        DiscoverService.Client client = ClientUtils.getSingleClient();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                client.cancel(serviceInfo);
                System.out.println("注销服务成功：name" + serviceInfo.getName()
                        + ", host：" + serviceInfo.getHost()
                        + ", port" + serviceInfo.getPort());
            } catch (TException e) {
                System.out.println("注销服务失败：name" + serviceInfo.getName()
                        + ", host：" + serviceInfo.getHost()
                        + ", port" + serviceInfo.getPort());
                e.printStackTrace();
            }
        }));
    }
}
