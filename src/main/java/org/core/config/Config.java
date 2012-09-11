package org.core.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.core.ChromeBenchmark;
import org.core.FirefoxBenchmark;

public class Config {

    @SuppressWarnings("rawtypes")
    public static final Class     BROWSER        = getBrowserClass();

    public static final Integer   SLEEP_INTERVAL = getConfig().getInt("Test.sleep_interval");
    public static final Integer   ITERATIONS     = getConfig().getInt("Test.iterations");
    public static final Boolean   WARMUP         = getConfig().getBoolean("Test.warmup");
    public static final String    PROXY_ADDRESS  = getConfig().getString("Test.proxy.address", null);
    public static final Integer   PROXY_PORT     = getConfig().getInteger("Test.proxy.port", null);
    public static final String[]  TEST_URLS      = getConfig().getStringArray("Test.urls.url");

    public static final Network[] TEST_NETWORKS  = Network.getNetworks(getConfig());

    public static final String    WANem_HOST     = getConfig().getString("WANem.host");
    public static final Boolean   WANem_BOTHWAYS = getConfig().getBoolean("WANem.bothways");

    private static Configuration  config;

    @SuppressWarnings("rawtypes")
    private static Class getBrowserClass() {
        switch (getConfig().getString("Test.browser", "Firefox")) {
            case "Firefox":
                return FirefoxBenchmark.class;
            case "Chrome":
                return ChromeBenchmark.class;
            default:
                return FirefoxBenchmark.class;
        }
    }

    private static Configuration getConfig() {
        if (config == null) {
            try {
                config = new XMLConfiguration("fruchtzwerg.xml");
            } catch (ConfigurationException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        return config;
    }

}
