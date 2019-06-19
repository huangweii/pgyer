package com.pgyer.simple.pinpoint.monitor.metric.cpu;

public class CpuLoadMetricSnapshot {
    private double jvmCpuUsage = 0.0;
    private double systemCpuUsage = 0.0;
    private long jvmCpuTime = 0L;
    private long jvmUserCpuTime = 0L;

    public CpuLoadMetricSnapshot() {
    }

    public CpuLoadMetricSnapshot(long jvmCpuTime, long jvmCpuUserTime) {
        this.jvmCpuTime = jvmCpuTime;
        this.jvmUserCpuTime = jvmCpuUserTime;
    }

    public CpuLoadMetricSnapshot(double jvmCpuUsage, double systemCpuUsage, long jvmCpuTime) {
        this.jvmCpuUsage = jvmCpuUsage;
        this.systemCpuUsage = systemCpuUsage;
        this.jvmCpuTime = jvmCpuTime;
    }

    public double getJvmCpuUsage() {
        return jvmCpuUsage;
    }

    public double getSystemCpuUsage() {
        return systemCpuUsage;
    }

    public void setJvmCpuUsage(double jvmCpuUsage) {
        this.jvmCpuUsage = jvmCpuUsage;
    }

    public void setSystemCpuUsage(double systemCpuUsage) {
        this.systemCpuUsage = systemCpuUsage;
    }

    public long getJvmCpuTime() {
        return jvmCpuTime;
    }

    public void setJvmCpuTime(long jvmCpuTime) {
        this.jvmCpuTime = jvmCpuTime;
    }

    public long getJvmUserCpuTime() {
        return jvmUserCpuTime;
    }

    public void setJvmUserCpuTime(long jvmUserCpuTime) {
        this.jvmUserCpuTime = jvmUserCpuTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("cpuTime=").append(jvmCpuTime);
        //sb.append(",jvmCpuUsage=").append(jvmCpuUsage);
        //sb.append(",systemCpuUsage=").append(systemCpuUsage);
        sb.append(",userTime=").append(jvmUserCpuTime);
        //sb.append('}');
        return sb.toString();
    }
}
