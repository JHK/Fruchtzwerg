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

import org.core.config.TestCase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class FirefoxBenchmark extends Benchmark {

    public FirefoxBenchmark(TestCase test) {
        super(test);
    }

    @Override
    protected WebDriver getWebDriver(TestCase test) {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("general.useragent.override", "Mozilla/5.0 (Linux; U; Android 2.1-update1; de-de; HTC Desire 1.19.161.5 Build/ERE27) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
        profile.setPreference("network.http.proxy.pipelining", true);
        
        if (test.getProxy() != null && !test.getProxy().equals("")) {
            String[] hostAndPort = test.getProxy().split(":");
            String host = hostAndPort[0];
            Integer port = Integer.valueOf(hostAndPort[1]);
            
            profile.setPreference("network.proxy.type", 1);
            profile.setPreference("network.proxy.http", host);
            profile.setPreference("network.proxy.http_port", port);
        }
        
        return new FirefoxDriver(profile);
    }
}
