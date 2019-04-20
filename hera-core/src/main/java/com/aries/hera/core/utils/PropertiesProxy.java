package com.aries.hera.core.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

@AllArgsConstructor
public class PropertiesProxy {
    private String propertiesName;

    public String readProperty(String key) {
        String value = "";
        try (InputStream is = PropertiesProxy.class.getClassLoader().getResourceAsStream(propertiesName)) {
            Properties p = new Properties();
            p.load(is);
            value = p.getProperty(key);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return value;
    }

    public Properties getProperties() {
        Properties p = new Properties();
        try (InputStream is = PropertiesProxy.class.getClassLoader().getResourceAsStream(propertiesName)) {
            p.load(is);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}