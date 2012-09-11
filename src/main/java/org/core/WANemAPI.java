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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

public class WANemAPI {

    private final WebDriver driver;
    private final String    baseUrl;
    private final boolean   bothways;

    /**
     * Get a new API connector to WANem
     * 
     * @param address The address of the host. e.g. "192.168.178.32"
     * @param bothways Is WANem used for sending and receiving packages
     */
    public WANemAPI(String address, Boolean bothways) {
        // this.driver = new FirefoxDriver();
        this.driver = new HtmlUnitDriver();
        this.baseUrl = "http://" + address + "/";
        this.bothways = bothways;
    }

    /**
     * Setting some values to WANem on the webinterface
     * 
     * @param bandwidth the bandwidth in kbit/sec
     * @param delay the delay in ms
     * @param packetLoss the packet loss in percent
     */
    public void set(int bandwidth, int delay, int packetLoss) {
        if (bothways) {
            delay = (int) delay / 2;
        }

        try {
            driver.get(baseUrl + "/WANem/start_advance.php");
            new Select(driver.findElement(By.name("selInt"))).selectByVisibleText("eth0");
            driver.findElement(By.name("btnAdvanced")).click();
            driver.findElement(By.name("txtDelay1")).clear();
            driver.findElement(By.name("txtDelay1")).sendKeys(String.valueOf(delay));
            driver.findElement(By.name("txtLoss1")).clear();
            driver.findElement(By.name("txtLoss1")).sendKeys(String.valueOf(packetLoss));
            driver.findElement(By.name("txtBandwidth1")).clear();
            driver.findElement(By.name("txtBandwidth1")).sendKeys(String.valueOf(bandwidth));
            new Select(driver.findElement(By.name("txtBandwidthAuto1"))).selectByVisibleText("Other");
            driver.findElement(By.name("btnApply")).click();
        } catch (Exception e) {
            System.err.println("WANem API not found");
            e.printStackTrace();
            driver.close();
        }
    }

    /**
     * Get the current bandwidth set in WANem
     * 
     * @return int in kbit/sec
     */
    public int getBandwidth() {
        return getField("txtBandwidth1");
    }

    /**
     * Get the current packet loss rate in WANem
     * 
     * @return int in percent
     */
    public int getPacketLoss() {
        return getField("txtLoss1");
    }

    /**
     * Get the current delay in WANem
     * 
     * @return int in ms
     */
    public int getDelay() {
        return getField("txtDelay1");
    }

    private int getField(String fieldName) {
        driver.get(baseUrl + "/WANem/start_advance.php");
        new Select(driver.findElement(By.name("selInt"))).selectByVisibleText("eth0");
        driver.findElement(By.name("btnAdvanced")).click();

        String value = driver.findElement(By.name(fieldName)).getAttribute("value");

        if (value == null || value.equals("")) {
            value = "0";
        }

        return Integer.valueOf(value);
    }

    @Override
    protected void finalize() {
        driver.close();
        try {
            super.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
