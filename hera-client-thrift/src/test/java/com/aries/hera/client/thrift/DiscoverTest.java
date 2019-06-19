package com.aries.hera.client.thrift;

import com.aries.hera.client.thrift.exception.CallFailedException;
import com.aries.hera.contract.thrift.dto.ServiceInfo;
import com.aries.hera.contract.thrift.service.DiscoverService;
import com.aries.hera.core.factory.ClientFactory;
import org.apache.thrift.transport.TTransportException;
import org.junit.Test;

import static com.aries.hera.core.constance.ConfConst.HERA_APP_NAME;

public class DiscoverTest {
    @Test
    public void randomTest() throws TTransportException {
        System.out.println(DiscoverClient.getRandomService("Hermes"));
    }

    @Test
    public void consistentHashTest() throws Exception {
//        System.out.println(DiscoverClient.getServiceByHash("Hermes","123"));
        ClientFactory.getClient().setData().forPath("/discover/Clotho", String.valueOf(System.currentTimeMillis()).getBytes());
//        System.out.println(StringUtils.getLong(ClientFactory.getClient().getData().forPath("/test")));

    }

    @Test
    public void timerTest() throws Exception {


    }

    public static void main(String[] args) throws CallFailedException, TTransportException, InterruptedException {
//        System.out.println("heiei");
//        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
//            System.out.println("haha");
//        }, 2, 2, TimeUnit.SECONDS);

        ThriftHelper.call(HERA_APP_NAME, DiscoverService.Client.class, client -> client.health(new ServiceInfo("haha", "lolo", 1111)));
    }
}
