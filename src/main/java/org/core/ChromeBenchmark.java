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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeBenchmark extends Benchmark {

    public ChromeBenchmark(TestCase test) {
        super(test);
    }

    @Override
    protected WebDriver getWebDriver(TestCase test) {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("user-agent=Mozilla/5.0 (Linux; U; Android 2.1-update1; de-de; HTC Desire 1.19.161.5 Build/ERE27) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
        
        if (test.getProxy() != null && !test.getProxy().equals("")) {
            if (test.getProxy().endsWith(".pac") || test.getProxy().endsWith(".js"))
                options.addArguments("proxy-pac-url=" + test.getProxy());
            else
                options.addArguments("proxy-server=" + test.getProxy());
        }

        return new ChromeDriver(options);
    }
}
