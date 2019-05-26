package com.aries.hera.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Objects;
import java.util.Properties;


public class PropertiesProxy {
    private static final Logger log = LoggerFactory.getLogger(PropertiesProxy.class);

    private String propertiesName;

    public PropertiesProxy(String propertiesName) {
        this.propertiesName = propertiesName;
    }

    public String readProperty(String key) {
        // 从classpath读取文件
        try (InputStream is = PropertiesProxy.class.getClassLoader().getResourceAsStream(propertiesName)) {
            if (is != null) {
                Properties p = new Properties();
                p.load(is);
                return p.getProperty(key);
            }
        } catch (IOException e) {
            log.error("PropertiesProxy error:{}", e.getMessage(), e);
        }

        // 如果从classpath没找到文件，那么把propertiesName当作绝对路径来寻找文件。
        try (InputStream is = new FileInputStream(propertiesName)) {
            Properties p = new Properties();
            p.load(is);
            return p.getProperty(key);
        } catch (IOException e) {
            log.error("PropertiesProxy error:{}", e.getMessage(), e);
        }

        // 如果都没有找到。
        return null;
    }

    public Properties getProperties() {
        Properties p = new Properties();
        // 从classpath读取文件
        try (InputStream is = PropertiesProxy.class.getClassLoader().getResourceAsStream(propertiesName)) {
            if (is != null) {
                p.load(is);
                return p;
            }
        } catch (IOException e) {
            log.error("PropertiesProxy error:{}", e.getMessage(), e);
        }

        // 如果从classpath没找到文件，那么把propertiesName当作绝对路径来寻找文件。
        try (InputStream is = new FileInputStream(propertiesName)) {
            p.load(is);
            return p;
        } catch (IOException e) {
            log.error("PropertiesProxy error:{}", e.getMessage(), e);
        }

        return p;
    }

    public void writeProperty(String key, String value) {
        Properties properties = new Properties();
        String file = Objects.requireNonNull(PropertiesProxy.class.getClassLoader().getResource(propertiesName)).getFile();
        try (InputStream is = new FileInputStream(propertiesName);
             OutputStream os = new FileOutputStream(file)) {
            properties.load(is);
            properties.setProperty(key, value);
            properties.store(os, key);
            os.flush();
        } catch (Exception e) {
            log.error("PropertiesProxy error:{}", e.getMessage(), e);
        }
    }
}