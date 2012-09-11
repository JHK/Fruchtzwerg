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

        if (test.getProxy() != null && !test.getProxy().equals("")) {
            if (test.getProxy().contains(":"))
                options.addArguments("proxy-server=" + test.getProxy());
            else
                options.addArguments("proxy-pac-url=" + test.getProxy());
        }

        return new ChromeDriver(options);
    }
}
