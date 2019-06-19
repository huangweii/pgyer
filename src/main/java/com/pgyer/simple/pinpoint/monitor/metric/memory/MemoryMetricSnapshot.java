package com.pgyer.simple.pinpoint.monitor.metric.memory;

public class MemoryMetricSnapshot {

    private final long heapMax;
    private final long heapUsed;
    private final long nonHeapMax;
    private final long nonHeapUsed;

    MemoryMetricSnapshot(long heapMax, long heapUsed, long nonHeapMax, long nonHeapUsed) {
        this.heapMax = heapMax;
        this.heapUsed = heapUsed;
        this.nonHeapMax = nonHeapMax;
        this.nonHeapUsed = nonHeapUsed;
    }

    public long getHeapMax() {
        return heapMax;
    }

    public long getHeapUsed() {
        return heapUsed;
    }

    public long getNonHeapMax() {
        return nonHeapMax;
    }

    public long getNonHeapUsed() {
        return nonHeapUsed;
    }

    @Override
    public String toString() {
        return "MemoryMetricSnapshot{" +
                "heapMax=" + heapMax +
                ", heapUsed=" + heapUsed +
                ", nonHeapMax=" + nonHeapMax +
                ", nonHeapUsed=" + nonHeapUsed +
                '}';
    }
}
