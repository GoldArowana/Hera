package com.aries.hera.client.thrift;

import com.aries.hera.client.thrift.exception.CallFailedException;
import com.aries.hera.contract.thrift.dto.ServiceInfo;
import com.aries.hera.core.utils.PropertiesProxy;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class MainTest {
    @Test
    public void testPing() {
        try {
            Assert.assertEquals(DiscoverClient.ping(), "pong");
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadMappingService() {
        PropertiesProxy propertiesProxy = new PropertiesProxy("/opt/config/server-mapping.porperties");
        Properties mappingInfos = propertiesProxy.getProperties();
        Enumeration<Object> names = mappingInfos.keys();
        while (names.hasMoreElements()) {
            Object name = names.nextElement();
            System.out.println(name);
        }
    }

    @Test
    public void t() throws CallFailedException, TTransportException, InterruptedException {
        DiscoverClient.registe(new ServiceInfo("Hermes", "localhost", 100));
        Thread.sleep(1000 * 60);
    }

    @Test
    public void tt() throws CallFailedException, TTransportException, InterruptedException {
        List<ServiceInfo> hermes = DiscoverClient.getServices("Hermes");
        System.out.println(hermes);
    }
}
