package com.scalepoint.automation.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class SystemUtils {

    private static String hostname = "Unknown";

    public static String getHostname() {
        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException ex) {
            System.out.println("Hostname can not be resolved");
        }
        return hostname;
    }

    public static String getResourcePath(String name) {
        return Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(name)).getPath().replaceFirst("^/", "");
    }
}
