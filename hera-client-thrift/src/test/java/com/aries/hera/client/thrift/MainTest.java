package com.aries.hera.client.thrift;

import org.apache.thrift.TException;
import org.junit.Assert;
import org.junit.Test;

public class MainTest {
    @Test
    public void testPing() {
        try {
            Assert.assertEquals(DiscoverClient.ping(), "pong");
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
