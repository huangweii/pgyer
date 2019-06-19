package com.pgyer.simple.pinpoint.monitor.collector;

import com.pgyer.simple.pinpoint.monitor.metric.AgentStatMetricSnapshot;
import com.pgyer.simple.pinpoint.monitor.metric.JvmGcMetricSnapshot;
import com.pgyer.simple.pinpoint.monitor.metric.cpu.CpuLoadMetricSnapshot;

public class AgentStatCollector implements AgentStatMetricCollector<AgentStatMetricSnapshot> {

    private final AgentStatMetricCollector<JvmGcMetricSnapshot> jvmGcMetricCollector;
    private final AgentStatMetricCollector<CpuLoadMetricSnapshot> cpuLoadMetricCollerctor;

    public AgentStatCollector(AgentStatMetricCollector<JvmGcMetricSnapshot> jvmGcMetricCollector,AgentStatMetricCollector<CpuLoadMetricSnapshot> cpuLoadMetricCollerctor){
        this.jvmGcMetricCollector=jvmGcMetricCollector;
        this.cpuLoadMetricCollerctor=cpuLoadMetricCollerctor;
    }

    @Override
    public AgentStatMetricSnapshot collect(long id) {
        AgentStatMetricSnapshot agentStat = new AgentStatMetricSnapshot();
        agentStat.setGc(jvmGcMetricCollector.collect(id));
        agentStat.setCpuLoad(cpuLoadMetricCollerctor.collect(id));
        return agentStat;
    }

}
