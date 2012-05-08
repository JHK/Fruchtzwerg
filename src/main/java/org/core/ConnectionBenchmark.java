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

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class ConnectionBenchmark {

    private final static int     SLEEP_INTERVAL = 50;

    private final boolean        warmup;
    private final int            times;
    private final FirefoxDriver  driver;
    private final URL            url;
    private Date                 start;
    private SummaryStatisticsExt browserSummary;
    private SummaryStatisticsExt rttSummary;

    public ConnectionBenchmark(String url, Integer times, Boolean warmup, String proxyAddress, Integer proxyPort)
            throws MalformedURLException {
        this.warmup = warmup;
        this.times = times;
        this.url = new URL(url);
        this.driver = new FirefoxDriver(getCapabilities(proxyAddress, proxyPort));
    }

    private DesiredCapabilities getCapabilities(String proxyAddress, Integer proxyPort) {
        DesiredCapabilities cap = new DesiredCapabilities();

        if (proxyAddress != null && proxyPort != null) {
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(proxyAddress + ":" + proxyPort);
            cap.setCapability(CapabilityType.PROXY, proxy);
        }

        return cap;
    }

    public void run() {
        start = new Date(System.currentTimeMillis());

        rttBenchmarkRun();
        browserBenchmarkRun();

        driver.close();
    }

    public void printSummary(PrintStream ps) {
        ps.println("--- Summary ---");
        ps.println("Url:\t\t" + this.url);
        ps.println("Started:\t" + start.toString());
        ps.println("Finished:\t" + new Date(System.currentTimeMillis()).toString());

        ps.println("\nRTT statistics:");
        displayStatisticalSummary(ps, rttSummary);

        ps.println("\nBrowser statistics:");
        displayStatisticalSummary(ps, browserSummary);
    }

    public String getUrl() {
        return this.url.toString();
    }

    public SummaryStatisticsExt getBrowserSummary() {
        return this.browserSummary;
    }

    public SummaryStatisticsExt getRttSummary() {
        return this.rttSummary;
    }

    private void displayStatisticalSummary(PrintStream ps, SummaryStatisticsExt summary) {
        if (summary == null)
            return;

        List<String> result = new LinkedList<String>();
        result.add("Iterations:\t" + Math.round(summary.getN()));
        result.add("Min:\t\t" + Math.round(summary.getMin()));
        result.add("LoQ:\t\t" + Math.round(summary.getLowerPercentile()));
        result.add("Median:\t\t" + Math.round(summary.getMedian()));
        result.add("UpQ:\t\t" + Math.round(summary.getUpperPercentile()));
        result.add("Max:\t\t" + Math.round(summary.getMax()));
        result.add("Mean:\t\t" + Math.round(summary.getMean()));
        result.add("Stddev:\t\t" + Math.round(summary.getStandardDeviation()));

        StringBuilder sb = new StringBuilder();
        for (String s : result) {
            sb.append(s + "\n");
        }
        ps.println(sb.toString());
    }

    private void browserBenchmarkRun() {
        if (warmup) {
            driver.get(getUrl());
            sleep(1000);

            for (int i = 0; i < 2; i++)
                driver.get(getUrl());
        }

        browserSummary = new SummaryStatisticsExt();

        for (int i = 0; i < times; i++) {
            long t0 = System.nanoTime();
            driver.get(getUrl());
            browserSummary.addValue((System.nanoTime() - t0) / 1000000L);
            sleep(SLEEP_INTERVAL);
        }
    }

    private void rttBenchmarkRun() {
        rttSummary = new SummaryStatisticsExt();

        if (warmup)
            for (int i = 0; i < 2; i++)
                ping(url.getHost());

        for (int i = 0; i < times; i++) {
            long t0 = System.nanoTime();
            ping(url.getHost());
            rttSummary.addValue((System.nanoTime() - t0) / 1000000L);
            sleep(SLEEP_INTERVAL);
        }
    }

    private void ping(String host) {
        try {
            Socket so = new Socket(InetAddress.getByName(host), 80);
            so.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sleep(int duration) {
        // give the system time load anything
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
