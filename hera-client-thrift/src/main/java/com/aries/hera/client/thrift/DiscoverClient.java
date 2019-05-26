package com.aries.hera.client.thrift;

import com.aries.hera.client.thrift.exception.CallFailedException;
import com.aries.hera.client.thrift.exception.ServiceNotFoundException;
import com.aries.hera.client.thrift.function.Try;
import com.aries.hera.contract.thrift.dto.RegistCode;
import com.aries.hera.contract.thrift.dto.ServiceInfo;
import com.aries.hera.contract.thrift.service.DiscoverService;
import com.aries.hera.core.utils.PropertiesProxy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiscoverClient {
    private static final Logger log = LoggerFactory.getLogger(DiscoverClient.class);

    private static final Set<ServiceInfo> needShutdownServices = new HashSet<>();

    private static final String host;
    private static final int port;

    static {
        PropertiesProxy propertiesProxy = new PropertiesProxy("/opt/config/server-mapping.porperties");
        String hera = propertiesProxy.readProperty("Hera");
        host = hera.split(":")[0];
        port = Integer.parseInt(hera.split(":")[1]);

        // jvm关闭时会执行这里
        Runtime.getRuntime().addShutdownHook(new Thread(DiscoverClient::shutAllDown));
    }

    public static String ping() throws TException {
        return ThriftHelper.call(DiscoverService.Client.class, Try.of((DiscoverService.Client::ping)), host, port);
    }

    public static List<ServiceInfo> getServices(String serviceName) throws TTransportException {
        PropertiesProxy propertiesProxy = new PropertiesProxy("/opt/config/server-mapping.porperties");
        return ThriftHelper.call(DiscoverService.Client.class, Try.of(client -> client.getServiceList(serviceName)), host, port);
    }

    public static ServiceInfo getFirstService(String serviceName) throws TTransportException, ServiceNotFoundException {
        List<ServiceInfo> services = getServices(serviceName);
        if (CollectionUtils.isEmpty(services)) {
            throw new ServiceNotFoundException("未找到服务: " + serviceName);
        }
        return services.get(0);
    }

    public static RegistCode registe(ServiceInfo serviceInfo) throws TTransportException, CallFailedException {
        ServiceInfo serviceInfoCopied = new ServiceInfo(serviceInfo);
        RegistCode state = ThriftHelper.call(DiscoverService.Client.class, Try.of(client -> client.registe(serviceInfoCopied)), host, port);
        if (state.getValue() == 1 || state.getValue() == 0) {
            needShutdownServices.add(serviceInfoCopied);
        }
        return state;
    }

    private static void shutAllDown() {
        for (ServiceInfo serviceInfo : needShutdownServices) {
            try {
                ThriftHelper.call(DiscoverService.Client.class, Try.of(client -> client.cancel(serviceInfo)), host, port);
                log.info("注销服务成功：name" + serviceInfo.getName()
                        + ", host:" + serviceInfo.getHost()
                        + ", port:" + serviceInfo.getPort());
            } catch (TException e) {
                log.error("注销服务失败：name" + serviceInfo.getName()
                        + ", host:" + serviceInfo.getHost()
                        + ", port:" + serviceInfo.getPort(), e);
            }
        }
    }
}
