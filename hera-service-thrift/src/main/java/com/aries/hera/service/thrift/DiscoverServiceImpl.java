package com.aries.hera.service.thrift;

import com.aries.hera.contract.thrift.dto.ServiceInfo;
import com.aries.hera.contract.thrift.service.DiscoverService;
import com.aries.hera.core.DiscoverUtils;
import com.aries.hera.core.pojo.ServicePojo;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class DiscoverServiceImpl implements DiscoverService.Iface {

    public String ping() throws TException {
        log.debug("收到一个ping请求，返回pong。");
        return "pong";
    }

    @Override
    public List<ServiceInfo> getServiceList(String serviceName) throws TException {
        List<ServicePojo> servicePojos = DiscoverUtils.discover(serviceName);
        if (servicePojos == null) {
            log.warn("服务：{}未找到。返回空列表", serviceName);
            return Collections.emptyList();
        }
        log.info("查询服务，查询到:{}", servicePojos);
        return servicePojos.stream().map(servicePojo2ServiceInfo).collect(Collectors.toList());
    }

    private Function<ServicePojo, ServiceInfo> servicePojo2ServiceInfo = (servicePojo) -> new ServiceInfo()
            .setName(servicePojo.getName())
            .setPort(servicePojo.getPort())
            .setHost(servicePojo.getHost());

    private Function<ServiceInfo, ServicePojo> serviceInfo2ServicePojo = (serviceInfo) -> ServicePojo.builder()
            .name(serviceInfo.getName())
            .host(serviceInfo.getHost())
            .port(serviceInfo.getPort())
            .build();

    /**
     * 返回1表示注册成功
     * 返回0表示，已经存在该节点，未变更。
     * 返回-1表示，异常，注册失败
     */
    @Override
    public short registe(ServiceInfo serviceInfo) throws TException {
        log.info("注册，name:{}, host:{}, ip:{}", serviceInfo.name, serviceInfo.host, serviceInfo.port);
        return DiscoverUtils.registe(serviceInfo2ServicePojo.apply(serviceInfo));
    }

    @Override
    public boolean cancel(ServiceInfo serviceInfo) throws TException {
        log.info("注销服务，name:{}, host:{}, ip:{}", serviceInfo.name, serviceInfo.host, serviceInfo.port);
        return DiscoverUtils.cancel(serviceInfo2ServicePojo.apply(serviceInfo));
    }

}