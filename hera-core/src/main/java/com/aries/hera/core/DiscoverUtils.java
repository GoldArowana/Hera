package com.aries.hera.core;

import com.aries.hera.core.pojo.ServicePojo;
import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;

import java.util.ArrayList;
import java.util.List;

public class DiscoverUtils {

    /**
     * 返回1表示注册成功
     * 返回0表示，已经存在该节点，未变更。
     * 返回-1表示，异常，注册失败
     */
    public static short registe(ServicePojo servicePojo) {
        CuratorFramework client = ClientFactory.getClient();
        try {
            // 此处最好加上分布式锁。
            String servicePath = "/discover/" + servicePojo.getName();
            if (client.checkExists().forPath(servicePath) == null) {
                client.create().forPath(servicePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String appPath = "/discover/" + servicePojo.getName() + "/" + servicePojo.getHost() + ":" + servicePojo.getPort();
            if (client.checkExists().forPath(appPath) == null) {
                client.create().forPath(appPath);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static List<ServicePojo> discover(String name) {
        ArrayList<ServicePojo> servicePojosList = Lists.newArrayList();
        CuratorFramework client = ClientFactory.getClient();
        try {
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
            e.printStackTrace();
        }
        return servicePojosList;
    }

    public static boolean cancel(ServicePojo servicePojo) {
        CuratorFramework client = ClientFactory.getClient();
        try {
            client.delete().forPath("/discover/" + servicePojo.getName() + "/" + servicePojo.getHost() + ":" + servicePojo.getPort());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
