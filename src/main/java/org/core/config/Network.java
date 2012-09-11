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

public class Network {

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
