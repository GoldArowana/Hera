package com.aries.hera.client.thrift;

import com.alibaba.fastjson.JSON;
import com.aries.hera.client.thrift.exception.ThriftRuntimeException;
import com.aries.hera.client.thrift.function.Try;
import com.aries.hera.contract.thrift.dto.ServiceInfo;
import com.aries.hera.core.utils.PropertiesProxy;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;

public class ThriftHelper {
    private static final Logger log = LoggerFactory.getLogger(ThriftHelper.class);
    private static final HashMap<String, Pair<String, Integer>> serviceInfoMap = new HashMap<>();

    static {
        PropertiesProxy propertiesProxy = new PropertiesProxy("/opt/config/server-mapping.porperties");
        Properties mappingInfos = propertiesProxy.getProperties();
        Set<Map.Entry<Object, Object>> infos = mappingInfos.entrySet();
        for (Map.Entry<Object, Object> info : infos) {
            String name = info.getKey().toString(); // 服务的名字
            String inf = info.getValue().toString(); // ip:端口
            String[] hostAndPort = inf.split(":");
            serviceInfoMap.put(name, Pair.of(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
        }
    }

    public static <Type, RET> RET call(String appName, Class<Type> typeClass, Try.UncheckedFunction<Type, RET> function) throws TTransportException {
        Pair<String, Integer> hostAndPort = serviceInfoMap.get(appName);
        String serviceName = typeClass.getEnclosingClass().getSimpleName();

        if (hostAndPort != null) {
            return call(typeClass, Try.of(function), serviceName, hostAndPort.getLeft(), hostAndPort.getRight());
        }

        ServiceInfo firstService = DiscoverClient.getFirstService(appName);
        return call(typeClass, Try.of(function), serviceName, firstService.getHost(), firstService.getPort());
    }

    public static <Type, RET> RET call(Class<Type> typeClass, Function<Type, RET> function, String host, int port) throws TTransportException {
        String serviceName = typeClass.getEnclosingClass().getSimpleName();
        return call(typeClass, function, serviceName, host, port);
    }

    public static <Type, RET> RET call(Class<Type> typeClass, Function<Type, RET> function, String serviceName, String host, int port) throws TTransportException {
        try (TTransport transport = new TSocket(host, port)) {
            TProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol multiplexedProtocol = new TMultiplexedProtocol(protocol, serviceName);
            transport.open();
            Type client = typeClass.getDeclaredConstructor(org.apache.thrift.protocol.TProtocol.class).newInstance(multiplexedProtocol);
            RET result = function.apply(client);
            if (log.isDebugEnabled()) {
                log.debug("result:{}", JSON.toJSONString(result));
            }
            return result;
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            log.error("newInstance实例化失败", e);
            throw new ThriftRuntimeException("newInstance实例化失败", e);
        } catch (NoSuchMethodException e) {
            log.error("该Server对象没有指定的构造器", e);
            throw new ThriftRuntimeException("该Server对象没有指定的构造器", e);
        }
    }
}
