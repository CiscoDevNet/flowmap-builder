package com.appdynamics.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetricData {
    private long metricId;
    private String metricPath;
    private String metricName;
    private String frequency;
    private MetricValue[] metricValues;
    private Long rolledUpValue;
    private Long maxRolledUpValue;


    public long getMetricId() {
        return metricId;
    }

    public void setMetricId(long metricId) {
        this.metricId = metricId;
    }

    public String getMetricPath() {
        return metricPath;
    }

    public void setMetricPath(String metricPath) {
        this.metricPath = metricPath;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public MetricValue[] getMetricValues() {
        return metricValues;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricValues(MetricValue[] metricValues) {
        this.metricValues = metricValues;
    }

    public Long getRolledUpValue() {
        return rolledUpValue;
    }

    public void setRolledUpValue(Long rolledUpValue) {
        this.rolledUpValue = rolledUpValue;
    }

    public Long getMaxRolledUpValue() {
        return maxRolledUpValue;
    }

    public void setMaxRolledUpValue(Long maxRolledUpValue) {
        this.maxRolledUpValue = maxRolledUpValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MetricData that = (MetricData) o;

        if (metricId != that.metricId)
            return false;

        if (frequency != that.frequency)
            return false;

        if (metricName != null ? !metricName.equals(that.metricName) : that.metricName != null)
            return false;

        if (metricPath != null ? !metricPath.equals(that.metricPath) : that.metricPath != null)
            return false;

        if (!Arrays.equals(metricValues, that.metricValues))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = metricPath != null ? metricPath.hashCode() : 0;

        result = 31 * result + (int) metricId;

        result = 31 * result + (metricName != null ? metricName.hashCode() : 0);

        result = 31 * result + (frequency != null ? frequency.hashCode() : 0);

        result = 31 * result + (metricValues != null ? Arrays.hashCode(metricValues) : 0);

        return result;
    }

    public MetricData skinClone(){
        MetricData c = new MetricData();
        c.setMaxRolledUpValue(getMaxRolledUpValue());
        c.setRolledUpValue(getRolledUpValue());
        c.setFrequency(getFrequency());
        c.setMetricId(getMetricId());
        c.setMetricName(getMetricName());
        c.setMetricPath(getMetricPath());
        return c;
    }
}
