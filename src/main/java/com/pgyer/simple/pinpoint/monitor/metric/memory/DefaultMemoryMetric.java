package com.pgyer.simple.pinpoint.monitor.metric.memory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class DefaultMemoryMetric implements MemoryMetric {

    private MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    public DefaultMemoryMetric() {
    }

    @Override
    public MemoryMetricSnapshot getSnapshot() {
        final MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        final MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        final long heapMax = heapMemoryUsage.getMax();
        final long heapUsed = heapMemoryUsage.getUsed();
        final long nonHeapMax = nonHeapMemoryUsage.getMax();
        final long nonHeapUsed = nonHeapMemoryUsage.getUsed();

        return new MemoryMetricSnapshot(heapMax, heapUsed, nonHeapMax, nonHeapUsed);
    }
}
