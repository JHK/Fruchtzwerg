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

import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.core.config.Config;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public abstract class Benchmark {

    private final URL            url;
    private Date                 start;
    private SummaryStatisticsExt browserSummary;

    public Benchmark(String url) throws MalformedURLException {
        this.url = new URL(url);
        this.browserSummary = new SummaryStatisticsExt();
    }

    protected abstract WebDriver getWebDriver();

    protected DesiredCapabilities getCapabilities() {
        DesiredCapabilities cap = new DesiredCapabilities();

        if (Config.PROXY_ADDRESS != null && Config.PROXY_PORT != null) {
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(Config.PROXY_ADDRESS + ":" + Config.PROXY_PORT);
            cap.setCapability(CapabilityType.PROXY, proxy);
        }

        return cap;
    }

    public void run() {
        final WebDriver driver = getWebDriver();

        start = new Date(System.currentTimeMillis());

        if (Config.WARMUP)
            for (int i = 0; i < 3; i++) {
                driver.get(getUrl());
                sleep(Config.SLEEP_INTERVAL);
            }

        for (int i = 0; i < Config.ITERATIONS; i++) {
            long t0 = System.nanoTime();
            driver.get(getUrl());
            browserSummary.addValue((System.nanoTime() - t0) / 1000000L);
            sleep(Config.SLEEP_INTERVAL);
        }

        driver.close();
    }

    public void printSummary(PrintStream ps) {
        ps.println("--- Summary ---");
        ps.println("Url:\t\t" + this.url);
        ps.println("Started:\t" + start.toString());
        ps.println("Finished:\t" + new Date(System.currentTimeMillis()).toString());

        ps.println("\nBrowser statistics:");
        List<String> result = new LinkedList<String>();
        result.add("Iterations:\t" + Math.round(browserSummary.getN()));
        result.add("Min:\t\t" + Math.round(browserSummary.getMin()));
        result.add("LoQ:\t\t" + Math.round(browserSummary.getLowerPercentile()));
        result.add("Median:\t\t" + Math.round(browserSummary.getMedian()));
        result.add("UpQ:\t\t" + Math.round(browserSummary.getUpperPercentile()));
        result.add("Max:\t\t" + Math.round(browserSummary.getMax()));
        result.add("Mean:\t\t" + Math.round(browserSummary.getMean()));
        result.add("Stddev:\t\t" + Math.round(browserSummary.getStandardDeviation()));

        StringBuilder sb = new StringBuilder();
        for (String s : result) {
            sb.append(s + "\n");
        }
        ps.println(sb.toString());
    }

    public String getUrl() {
        return this.url.toString();
    }

    public SummaryStatisticsExt getBrowserSummary() {
        return this.browserSummary;
    }

    private void sleep(int duration) {
        // give the system time load anything
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
