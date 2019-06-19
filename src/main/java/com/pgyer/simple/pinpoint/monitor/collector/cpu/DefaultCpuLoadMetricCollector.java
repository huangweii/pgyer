package com.pgyer.simple.pinpoint.monitor.collector.cpu;

import com.pgyer.simple.pinpoint.monitor.collector.AgentStatMetricCollector;
import com.pgyer.simple.pinpoint.monitor.metric.cpu.CpuLoadMetric;
import com.pgyer.simple.pinpoint.monitor.metric.cpu.CpuLoadMetricSnapshot;

public class DefaultCpuLoadMetricCollector implements AgentStatMetricCollector<CpuLoadMetricSnapshot> {
    private final CpuLoadMetric cpuLoadMetric;

    public DefaultCpuLoadMetricCollector(CpuLoadMetric cpuLoadMetric) {
        this.cpuLoadMetric = cpuLoadMetric;
    }

    @Override
    public CpuLoadMetricSnapshot collect(long id) {
        final CpuLoadMetricSnapshot snapshot = cpuLoadMetric.getSnapshot(id);
        return snapshot;
    }
}
