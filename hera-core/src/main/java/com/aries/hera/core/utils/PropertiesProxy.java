package com.aries.hera.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class PropertiesProxy {
    private String propertiesName;

    public PropertiesProxy(String propertiesName) {
        this.propertiesName = propertiesName;
    }

    public String readProperty(String key) {
        String value = "";
        try (InputStream is = PropertiesProxy.class.getClassLoader().getResourceAsStream(propertiesName)) {
            Properties p = new Properties();
            p.load(is);
            value = p.getProperty(key);
        } catch (IOException e) {
            log.error("PropertiesProxy error:{}", e.getMessage(), e);
        }
        return value;
    }

    public Properties getProperties() {
        Properties p = new Properties();
        try (InputStream is = PropertiesProxy.class.getClassLoader().getResourceAsStream(propertiesName)) {
            p.load(is);
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