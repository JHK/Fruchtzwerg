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

import org.core.config.Config;
import org.core.config.Network;
import org.core.config.TestCase;

public class Main {

    public static void main(String[] args) throws Exception {
        WANemAPI wanem = new WANemAPI(Config.WANem_HOST, Config.WANem_BOTHWAYS);

        for (TestCase test : Config.TESTS) {
            Network network = test.getNetwork();
            wanem.set(network.getBandwidth(), network.getDelay(), network.getPacketLoss());

            @SuppressWarnings("rawtypes")
            Class browser = Benchmark.getBrowserClass(test.getBrowser());
            @SuppressWarnings("unchecked")
            Benchmark bench = (Benchmark) browser.getConstructor(TestCase.class).newInstance(test);

            bench.run();
            bench.printSummary(System.out);

            if (Config.HALT_BETWEEN_TESTS) {
                System.err.println("Press enter for next test");
                System.in.read();
            }
        }
    }
}