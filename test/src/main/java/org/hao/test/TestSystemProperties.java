package org.hao.test;

import java.io.IOException;
import java.util.Properties;

public class TestSystemProperties {

    private final static Properties properties;

    static {
        properties = new Properties(System.getProperties());
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("test.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void main(String[] args) throws Exception {
        if (properties.getProperty("aaa") != null) System.out.println(properties.getProperty("aaa"));
        if (properties.getProperty("bbb") != null) System.out.println(properties.getProperty("bbb"));
    }

}
