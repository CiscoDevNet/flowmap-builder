package com.appdynamics.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricValue {
    private long startTimeInMillis;

    private long value;
    private long min;
    private long max;
    private long current;

    private long sum;
    private long count;
    private double standardDeviation;

    private int occurrences;
    private boolean useRange; // true if min/max has been set

    public long getStartTimeInMillis() {
        return startTimeInMillis;
    }

    public void setStartTimeInMillis(Date startTimeInMillis) {
        this.startTimeInMillis = startTimeInMillis.getTime();
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getSum() {
        return sum;
    }

    public long getCount() {
        return count;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public boolean isUseRange() {
        return useRange;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }

    public void setUseRange(boolean useRange) {
        this.useRange = useRange;
    }

    @Override
    public String toString() {
        return "Value:" + value + ", Min:" + min + ", Max:" + max + ", Sum:" + sum +
                ", Count:" + count + ", Stddev: "
                + getStandardDeviation() + " Occurrences: " + getOccurrences();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MetricValue that = (MetricValue) o;

        if (count != that.count)
            return false;

        if (current != that.current)
            return false;

        if (max != that.max)
            return false;

        if (min != that.min)
            return false;

        if (occurrences != that.occurrences)
            return false;

        if (Double.compare(that.standardDeviation, standardDeviation) != 0)
            return false;

        if (startTimeInMillis != that.startTimeInMillis)
            return false;

        if (sum != that.sum)
            return false;

        if (useRange != that.useRange)
            return false;

        if (value != that.value)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;

        result = (int) (startTimeInMillis ^ (startTimeInMillis >>> 32));

        result = 31 * result + (int) (value ^ (value >>> 32));

        result = 31 * result + (int) (min ^ (min >>> 32));

        result = 31 * result + (int) (max ^ (max >>> 32));

        result = 31 * result + (int) (current ^ (current >>> 32));

        result = 31 * result + (int) (sum ^ (sum >>> 32));

        result = 31 * result + (int) (count ^ (count >>> 32));

        temp = Double.doubleToLongBits(standardDeviation);

        result = 31 * result + (int) (temp ^ (temp >>> 32));

        result = 31 * result + occurrences;

        result = 31 * result + (useRange ? 1 : 0);

        return result;
    }
}
