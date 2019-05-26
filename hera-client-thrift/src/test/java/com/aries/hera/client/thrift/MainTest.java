package com.aries.hera.client.thrift;

import com.aries.hera.core.utils.PropertiesProxy;
import org.apache.thrift.TException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Enumeration;
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
        while(names.hasMoreElements()){
            Object name = names.nextElement();
            System.out.println(name);
        }
    }
}
