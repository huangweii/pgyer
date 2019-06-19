package com.pgyer.simple.pinpoint.monitor.collector;

public interface AgentStatMetricCollector<T> {
    T collect(long id);
}
