package org.core.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

public class Config {

    public static final Integer    SLEEP_INTERVAL     = getConfig().getInt("Global.sleep_interval");
    public static final Integer    ITERATIONS         = getConfig().getInt("Global.iterations");
    public static final Boolean    WARMUP             = getConfig().getBoolean("Global.warmup");
    public static final Boolean    HALT_BETWEEN_TESTS = getConfig().getBoolean("Global.halt_between_tests");

    public static final String     WANem_HOST         = getConfig().getString("WANem.host");
    public static final Boolean    WANem_BOTHWAYS     = getConfig().getBoolean("WANem.bothways");

    public static final TestCase[] TESTS              = TestCase.getTestCases(getConfig());

    private static Configuration   config;

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
