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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.core.config.TestCase;
import org.openqa.selenium.WebDriver;

public abstract class Benchmark {

    @SuppressWarnings("rawtypes")
    public static Class getBrowserClass(String browserString) {
        switch (browserString) {
            case "Firefox":
                return FirefoxBenchmark.class;
            case "Chrome":
                return ChromeBenchmark.class;
            default:
                return FirefoxBenchmark.class;
        }
    }

    private final TestCase       test;
    private Date                 start;
    private SummaryStatisticsExt browserSummary;

    public Benchmark(TestCase test) {
        this.test = test;
        this.browserSummary = new SummaryStatisticsExt();
    }

    protected abstract WebDriver getWebDriver(TestCase test);

    public void run() {
        final WebDriver driver = getWebDriver(test);

        start = new Date(System.currentTimeMillis());

        if (test.getWarmup())
            for (int i = 0; i < 3; i++) {
                driver.get(test.getUrl());
                sleep(test.getSleepInterval());
            }

        for (int i = 0; i < test.getIterations(); i++) {
            long t0 = System.nanoTime();
            driver.get(test.getUrl());
            browserSummary.addValue((System.nanoTime() - t0) / 1000000L);
            sleep(test.getSleepInterval());
        }

        driver.close();
    }

    public void printSummary(PrintStream ps) {
        ps.println("--- Summary ---");
        ps.println("Testcase:");
        ps.println("Url:\t\t" + test.getUrl());
        ps.println("Browser:\t" + test.getBrowser());
        ps.println("Proxy:\t\t" + test.getProxy());
        ps.println("Bandwidth:\t" + test.getNetwork().getBandwidth());
        ps.println("Delay:\t\t" + test.getNetwork().getDelay());
        ps.println("Packet loss:\t" + test.getNetwork().getPacketLoss());
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

    private void sleep(int duration) {
        // give the system time load anything
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
