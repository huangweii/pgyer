package com.pgyer.simple.pinpoint.monitor;

import com.pgyer.simple.pinpoint.monitor.metric.AgentStatMetricSnapshot;

public interface AgentStatMonitor {
    AgentStatMetricSnapshot start(long id);

    void stop();
}
