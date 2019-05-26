package com.aries.hera.client.thrift;

import com.aries.hera.client.thrift.exception.CallFailedException;
import com.aries.hera.client.thrift.exception.ServiceNotFoundException;
import com.aries.hera.contract.thrift.dto.RegistCode;
import com.aries.hera.contract.thrift.dto.ServiceInfo;
import com.aries.hera.contract.thrift.service.DiscoverService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.aries.hera.core.constance.ConfConst.HERA_APP_NAME;

public class DiscoverClient {
    private static final Logger log = LoggerFactory.getLogger(DiscoverClient.class);

    private static final Set<ServiceInfo> needShutdownServices = new HashSet<>();

    static {
        // jvm关闭时会执行这里
        Runtime.getRuntime().addShutdownHook(new Thread(DiscoverClient::shutAllDown));
    }

    public static String ping() throws TException {
        return ThriftHelper.call(HERA_APP_NAME, DiscoverService.Client.class, DiscoverService.Client::ping);
    }

    public static List<ServiceInfo> getServices(String serviceName) throws TTransportException {
        return ThriftHelper.call(HERA_APP_NAME, DiscoverService.Client.class, client -> client.getServiceList(serviceName));
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
        RegistCode state = ThriftHelper.call(HERA_APP_NAME, DiscoverService.Client.class, client -> client.registe(serviceInfoCopied));
        if (state.getValue() == 1 || state.getValue() == 0) {
            needShutdownServices.add(serviceInfoCopied);
        }
        return state;
    }

    private static void shutAllDown() {
        for (ServiceInfo serviceInfo : needShutdownServices) {
            try {
                ThriftHelper.call("Hera", DiscoverService.Client.class, client -> client.cancel(serviceInfo));
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
