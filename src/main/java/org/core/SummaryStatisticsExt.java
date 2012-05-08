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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.StatUtils;

public class SummaryStatisticsExt {

    private List<Double> values;

    public SummaryStatisticsExt() {
        values = new ArrayList<Double>();
    }

    public void addValue(double val) {
        values.add(val);
    }

    public double getLowerPercentile() {
        return StatUtils.percentile(getValues(), 25);
    }

    public double getMedian() {
        return StatUtils.percentile(getValues(), 50);
    }

    public double getUpperPercentile() {
        return StatUtils.percentile(getValues(), 75);
    }

    public double getMean() {
        return StatUtils.mean(getValues());
    }

    public double getN() {
        return values.size();
    }

    public double getMax() {
        return StatUtils.max(getValues());
    }

    public double getMin() {
        return StatUtils.min(getValues());
    }

    private double[] getValues() {
        double[] values = new double[this.values.size()];

        for (int x = 0; x != this.values.size(); x++) {
            values[x] = this.values.get(x) == null ? 0 : this.values.get(x);
        }

        return values;
    }

    public double getStandardDeviation() {
        return Math.sqrt(StatUtils.variance(getValues()));
    }
}
