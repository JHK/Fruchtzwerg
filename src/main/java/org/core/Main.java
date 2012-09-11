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

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import org.core.config.Config;
import org.core.config.Network;

public class Main {

    public static void main(String[] args) throws MalformedURLException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        WANemAPI wanem = new WANemAPI(Config.WANem_HOST, Config.WANem_BOTHWAYS);

        for (String host : Config.TEST_URLS) {
            for (Network network : Config.TEST_NETWORKS) {
                wanem.set(network.getBandwidth(), network.getDelay(), network.getPacketLoss());
                @SuppressWarnings("unchecked")
                Benchmark bench = (Benchmark) Config.BROWSER.getConstructor(String.class).newInstance(host);
                bench.run();
                bench.printSummary(System.out);
                network.printConfig(System.out);
            }
        }
    }
}