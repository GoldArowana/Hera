package com.aries.hera.core;

import com.aries.hera.core.utils.PropertiesProxy;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import static com.aries.hera.core.constance.ConfConst.CONF_PROPERTIES;
import static com.aries.hera.core.constance.ConfConst.REDIS_HOST;
import static com.aries.hera.core.constance.ConfConst.REDIS_PORT;

public class JedisPoolFactory {
    private static JedisPool pool = null;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxTotal(200);
        // 设置最大空闲数
        config.setMaxIdle(8);
        // 设置最大等待时间
        config.setMaxWaitMillis(1000 * 100);
        // 在borrow一个jedis实例时，是否需要验证，若为true，则所有jedis实例均是可用的
        config.setTestOnBorrow(true);

        String redisHost = new PropertiesProxy(CONF_PROPERTIES).readProperty(REDIS_HOST);
        int redisPort = Integer.parseInt(new PropertiesProxy(CONF_PROPERTIES).readProperty(REDIS_PORT));
        pool = new JedisPool(config, redisHost, redisPort, 3000);
    }

    public static JedisPool getJedisPool() {
        return pool;
    }
}
