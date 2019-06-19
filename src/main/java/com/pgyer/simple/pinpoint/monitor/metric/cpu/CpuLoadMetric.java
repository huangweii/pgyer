package com.pgyer.simple.pinpoint.monitor.metric.cpu;

public interface CpuLoadMetric {
    CpuLoadMetricSnapshot getSnapshot(long id);
}
