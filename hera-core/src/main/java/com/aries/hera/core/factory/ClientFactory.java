package com.aries.hera.core.factory;

import com.aries.hera.core.utils.PropertiesProxy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryForever;

import static com.aries.hera.core.constance.ConfConst.CONF_PROPERTIES;
import static com.aries.hera.core.constance.ConfConst.ZK_ADDRESS;

public class ClientFactory {
    private static volatile CuratorFramework client = null;

    public static CuratorFramework getClient() {
        if (client == null) {
            synchronized (ClientFactory.class) {
                if (client == null) {
                    String zookeeperAddress = new PropertiesProxy(CONF_PROPERTIES).readProperty(ZK_ADDRESS);
                    client = CuratorFrameworkFactory.builder()
                            .connectString(zookeeperAddress)//zkClint连接地址
                            .connectionTimeoutMs(2000)//连接超时时间
                            .sessionTimeoutMs(10000)//会话超时时间
                            .retryPolicy(new RetryForever(1000))//重试策略
                            .build();

                    client.start();
                }
            }
        }
        return client;
    }
}
