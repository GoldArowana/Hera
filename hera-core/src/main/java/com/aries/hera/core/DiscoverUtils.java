package com.aries.hera.core;

import com.aries.hera.core.pojo.ServicePojo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DiscoverUtils {

    /**
     * 返回1表示注册成功
     * 返回0表示，已经存在该节点，未变更。
     * 返回-1表示，异常，注册失败
     */
    public static short registe(ServicePojo servicePojo) {
        log.info("【注册服务】准备中, name:{}, host:{}, port:{}", servicePojo.getName(), servicePojo.getHost(), servicePojo.getPort());
        CuratorFramework client = ClientFactory.getClient();
        try {
            // 此处最好加上分布式锁。
            String servicePath = "/discover/" + servicePojo.getName();
            if (client.checkExists().forPath(servicePath) == null) {
                client.create().forPath(servicePath);
            }
        } catch (Exception e) {
            log.error("【注册服务】失败, name:{}, host:{}, port:{}", servicePojo.getName(), servicePojo.getHost(), servicePojo.getPort(), e);
        }

        try {
            String appPath = "/discover/" + servicePojo.getName() + "/" + servicePojo.getHost() + ":" + servicePojo.getPort();
            if (client.checkExists().forPath(appPath) == null) {
                client.create().forPath(appPath);
                log.info("【注册服务】成功, name:{}, host:{}, port:{}", servicePojo.getName(), servicePojo.getHost(), servicePojo.getPort());
                return 1;
            } else {
                log.warn("【注册服务】不生效（该服务已被注册）, name:{}, host:{}, port:{}", servicePojo.getName(), servicePojo.getHost(), servicePojo.getPort());
                return 0;
            }
        } catch (Exception e) {
            log.error("【注册服务】失败,error:{}", e.getMessage(), e);
            return -1;
        }
    }

    public static List<ServicePojo> discover(String name) {
        ArrayList<ServicePojo> servicePojosList = Lists.newArrayList();
        CuratorFramework client = ClientFactory.getClient();
        try {
            log.info("【查询服务】准备中, name:{}", name);
            List<String> serviceAddressList = client.getChildren().forPath("/discover/" + name);
            for (String address : serviceAddressList) {
                String[] ipPortArr = address.split(":");
                ServicePojo servicePojo = ServicePojo.builder()
                        .name(name)
                        .host(ipPortArr[0])
                        .port(Integer.parseInt(ipPortArr[1]))
                        .build();
                servicePojosList.add(servicePojo);
            }
        } catch (Exception e) {
            log.error("【查询服务】失败,error:{}", e.getMessage(), e);
        }
        return servicePojosList;
    }

    public static boolean cancel(ServicePojo servicePojo) {
        CuratorFramework client = ClientFactory.getClient();
        try {
            log.info("【注销服务】准备中, name:{}, host:{} , port:{}", servicePojo.getName(), servicePojo.getHost(), servicePojo.getPort());
            client.delete().forPath("/discover/" + servicePojo.getName() + "/" + servicePojo.getHost() + ":" + servicePojo.getPort());
            return true;
        } catch (Exception e) {
            log.error("【注销服务】失败,error:{}", e.getMessage(), e);
            return false;
        }
    }
}
