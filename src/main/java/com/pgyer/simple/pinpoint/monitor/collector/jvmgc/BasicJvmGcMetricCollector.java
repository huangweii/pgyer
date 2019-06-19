package com.pgyer.simple.pinpoint.monitor.collector.jvmgc;

import com.pgyer.simple.pinpoint.monitor.collector.AgentStatMetricCollector;
import com.pgyer.simple.pinpoint.monitor.metric.JvmGcMetricSnapshot;
import com.pgyer.simple.pinpoint.monitor.metric.memory.MemoryMetric;
import com.pgyer.simple.pinpoint.monitor.metric.memory.MemoryMetricSnapshot;

public class BasicJvmGcMetricCollector implements AgentStatMetricCollector<JvmGcMetricSnapshot> {

    private final MemoryMetric memoryMetric;

    public BasicJvmGcMetricCollector(MemoryMetric memoryMetric){
        this.memoryMetric=memoryMetric;
    }

    @Override
    public JvmGcMetricSnapshot collect(long id) {
        final MemoryMetricSnapshot memoryMetricSnapshot = memoryMetric.getSnapshot();

        final JvmGcMetricSnapshot jvmGcMetricSnapshot = new JvmGcMetricSnapshot();
        jvmGcMetricSnapshot.setJvmMemoryHeapMax(memoryMetricSnapshot.getHeapMax());
        jvmGcMetricSnapshot.setJvmMemoryHeapUsed(memoryMetricSnapshot.getHeapUsed());
        jvmGcMetricSnapshot.setJvmMemoryNonHeapMax(memoryMetricSnapshot.getNonHeapMax());
        jvmGcMetricSnapshot.setJvmMemoryNonHeapUsed(memoryMetricSnapshot.getNonHeapUsed());

        return jvmGcMetricSnapshot;
    }
}
