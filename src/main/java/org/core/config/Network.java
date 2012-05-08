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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;

public class Network {

    public static Network[] getNetworks(Configuration config) {
        List<Object> bandwidth = config.getList("Test.networks.network.bandwidth");
        List<Object> delay = config.getList("Test.networks.network.delay");
        List<Object> packetLoss = config.getList("Test.networks.network.packet-loss");

        List<Network> networks = new ArrayList<Network>();

        for (int i = 0; i < bandwidth.size(); i++) {
            Integer b = new Integer((String) bandwidth.get(i));
            Integer d = new Integer((String) delay.get(i));
            Integer p = new Integer((String) packetLoss.get(i));

            networks.add(new Network(b, d, p));
        }

        return networks.toArray(new Network[networks.size()]);
    }

    private int bandwidth;
    private int delay;
    private int packetLoss;

    public Network(int bandwidth, int delay) {
        this.setBandwidth(bandwidth);
        this.setDelay(delay);
        this.setPacketLoss(0);
    }

    public Network(int bandwidth, int delay, int packetLoss) {
        this(bandwidth, delay);
        this.setPacketLoss(packetLoss);
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getPacketLoss() {
        return packetLoss;
    }

    public void setPacketLoss(int packetLoss) {
        this.packetLoss = packetLoss;
    }

    public void printConfig(PrintStream ps) {
        ps.println("Network config:");
        ps.println("Bandwidth:\t" + getBandwidth());
        ps.println("Delay:\t\t" + getDelay());
        ps.println("Packet loss:\t" + getPacketLoss());
    }
}
