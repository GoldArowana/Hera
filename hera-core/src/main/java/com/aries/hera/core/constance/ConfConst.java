package com.aries.hera.core.constance;

public class ConfConst {
    /**
     * 本项目的app名。服务注册/发现将使用本名字。/opt/config/server-mapping.porperties里也会使用本名字
     */
    public static final String HERA_APP_NAME = "Hera";

    /**
     * 本系统启动时的端口
     */
    public static final int PORT = 8020;

    /**
     * 这是一个配置文件的名字，里面有zookeeper客户端的ip地址和端口。
     */
    public static final String CONF_PROPERTIES = "conf.properties";

    /**
     * `zkAddress`是conf.properties这个配置文件里的一个key，
     */
    public static final String ZK_ADDRESS = "zkAddress";
}
