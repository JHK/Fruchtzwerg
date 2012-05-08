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
package org.core;

import java.net.MalformedURLException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.core.config.Network;

public class Main {

    public static void main(String[] args) throws ConfigurationException, MalformedURLException {
        Configuration config = getConfig();
        Integer iterations = config.getInt("Test.iterations");
        Boolean warmup = config.getBoolean("Test.warmup");

        String proxyAddress = config.getString("Test.proxy.address", null);
        Integer proxyPort = config.getInteger("Test.proxy.port", null);

        String WANemHost = config.getString("WANem.host");
        String[] TestUrls = config.getStringArray("Test.urls.url");
        Network[] TestNetworks = Network.getNetworks(config);

        WANemAPI wanem = new WANemAPI(WANemHost, config.getBoolean("WANem.bothways"));

        for (String host : TestUrls) {
            for (Network network : TestNetworks) {
                wanem.set(network.getBandwidth(), network.getDelay(), network.getPacketLoss());
                ConnectionBenchmark bench = new ConnectionBenchmark(host, iterations, warmup, proxyAddress, proxyPort);
                bench.run();
                bench.printSummary(System.out);
                network.printConfig(System.out);
            }
        }
    }

    private static Configuration getConfig() throws ConfigurationException {
        return new XMLConfiguration("fruchtzwerg.xml");
    }
}