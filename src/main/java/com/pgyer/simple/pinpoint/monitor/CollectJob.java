package com.pgyer.simple.pinpoint.monitor;

import com.pgyer.simple.pinpoint.monitor.collector.AgentStatMetricCollector;
import com.pgyer.simple.pinpoint.monitor.metric.AgentStatMetricSnapshot;

import java.util.concurrent.Callable;

public class CollectJob implements Callable<AgentStatMetricSnapshot> {
    private final AgentStatMetricCollector<AgentStatMetricSnapshot> agentStatCollector;
    private final long ThreadId;

    public CollectJob(AgentStatMetricCollector<AgentStatMetricSnapshot> agentStatCollector,long id){
        this.agentStatCollector=agentStatCollector;
        this.ThreadId=id;
    }
    @Override
    public AgentStatMetricSnapshot call() {
        final AgentStatMetricSnapshot agentStat = agentStatCollector.collect(this.ThreadId);
        return agentStat;
    }
}
