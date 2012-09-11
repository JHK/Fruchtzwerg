/**
 * Copyright (C) 2012 Julian Knocke
 * 
 * This file is part of Fruchtzwerg.
 * 
 * Fruchtzwerg is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Fruchtzwerg is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Fruchtzwerg. If not, see <http://www.gnu.org/licenses/>.
 */
package org.core.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;

public class TestCase {

    public static TestCase[] getTestCases(Configuration config) {
        String[] url_array = config.getStringArray("Tests.TestCase.url");
        String[] proxy_array = config.getStringArray("Tests.TestCase.proxy");

        String[] bandwidth_array = config.getStringArray("Tests.TestCase.network.bandwidth");
        String[] delay_array = config.getStringArray("Tests.TestCase.network.delay");
        String[] packetLoss_array = config.getStringArray("Tests.TestCase.network.packet-loss");

        List<TestCase> tests = new ArrayList<TestCase>();

        for (int i = 0; i < url_array.length; i++) {
            Integer bandwidth = new Integer(bandwidth_array[i]);
            Integer delay = new Integer(delay_array[i]);
            Integer packetLoss = new Integer(packetLoss_array[i]);
            Network network = new Network(bandwidth, delay, packetLoss);

            TestCase testCase = new TestCase(url_array[i], proxy_array[i], network);
            tests.add(testCase);
        }

        return tests.toArray(new TestCase[tests.size()]);
    }

    private final String  url;
    private final Network network;
    private final String  proxy;

    public TestCase(String url, String proxy, Network network) {
        this.url = url;
        this.network = network;
        this.proxy = proxy;
    }

    public String getProxy() {
        return proxy;
    }

    public String getUrl() {
        return url;
    }

    public Network getNetwork() {
        return network;
    }

    public String getBrowser() {
        return Config.BROWSER;
    }

    public Integer getIterations() {
        return Config.ITERATIONS;
    }

    public Boolean getWarmup() {
        return Config.WARMUP;
    }

    public Integer getSleepInterval() {
        return Config.SLEEP_INTERVAL;
    }
}
