package com.aries.hera.client.thrift;

import com.aries.hera.core.factory.ClientFactory;
import com.mysql.cj.util.StringUtils;
import org.apache.thrift.transport.TTransportException;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DiscoverTest {
    @Test
    public void randomTest() throws TTransportException {
        System.out.println(DiscoverClient.getRandomService("Hermes"));
    }

    @Test
    public void consistentHashTest() throws Exception {
//        System.out.println(DiscoverClient.getServiceByHash("Hermes","123"));
//        ClientFactory.getClient().setData().forPath("/test", String.valueOf(System.currentTimeMillis()).getBytes());
        System.out.println(StringUtils.getLong(ClientFactory.getClient().getData().forPath("/test")));

    }

    @Test
    public void timerTest() throws Exception {


    }

    public static void main(String[] args) {
        System.out.println("heiei");
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            System.out.println("haha");
        }, 2, 2, TimeUnit.SECONDS);
    }
}
