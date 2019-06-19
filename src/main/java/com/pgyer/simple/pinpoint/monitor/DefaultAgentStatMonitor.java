package com.pgyer.simple.pinpoint.monitor;

import com.pgyer.simple.pinpoint.monitor.collector.AgentStatCollector;
import com.pgyer.simple.pinpoint.monitor.collector.AgentStatMetricCollector;
import com.pgyer.simple.pinpoint.monitor.collector.cpu.DefaultCpuLoadMetricCollector;
import com.pgyer.simple.pinpoint.monitor.collector.jvmgc.BasicJvmGcMetricCollector;
import com.pgyer.simple.pinpoint.monitor.metric.AgentStatMetricSnapshot;
import com.pgyer.simple.pinpoint.monitor.metric.JvmGcMetricSnapshot;
import com.pgyer.simple.pinpoint.monitor.metric.cpu.CpuLoadMetric;
import com.pgyer.simple.pinpoint.monitor.metric.cpu.CpuLoadMetricSnapshot;
import com.pgyer.simple.pinpoint.monitor.metric.cpu.DefaultCpuLoadMetric;
import com.pgyer.simple.pinpoint.monitor.metric.memory.DefaultMemoryMetric;
import com.pgyer.simple.pinpoint.monitor.metric.memory.MemoryMetric;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DefaultAgentStatMonitor implements AgentStatMonitor {

    //private final CollectJob collectJob;
    private final AgentStatMetricCollector<AgentStatMetricSnapshot> agentStatCollector;
    private final ExecutorService exs;

    private static final DefaultAgentStatMonitor instance = new DefaultAgentStatMonitor();

    private DefaultAgentStatMonitor() {
        this.agentStatCollector = creatAgentStatCollector();
        //this.collectJob = new CollectJob(agentStatCollector);
        exs = Executors.newCachedThreadPool();
    }

    public static DefaultAgentStatMonitor getInstance() {
        return instance;
    }

    private AgentStatMetricCollector<AgentStatMetricSnapshot> creatAgentStatCollector() {
        MemoryMetric memoryMetric = new DefaultMemoryMetric();
        CpuLoadMetric cpuLoadMetric = new DefaultCpuLoadMetric();
        AgentStatMetricCollector<JvmGcMetricSnapshot> jvmGcMetricCollector = new BasicJvmGcMetricCollector(memoryMetric);
        AgentStatMetricCollector<CpuLoadMetricSnapshot> cpuLoadMetricCollerctor = new DefaultCpuLoadMetricCollector(cpuLoadMetric);
        return new AgentStatCollector(jvmGcMetricCollector, cpuLoadMetricCollerctor);
    }

    @Override
    public AgentStatMetricSnapshot start(long id) {
        CollectJob collectJob = new CollectJob(agentStatCollector,id);
        List<Future<AgentStatMetricSnapshot>> al = new ArrayList<Future<AgentStatMetricSnapshot>>();
        al.add(exs.submit(collectJob));
        if (al.size() != 0) {
            try {
                return al.get(0).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("monitor result is null");
        return null;
    }

    @Override
    public void stop() {

    }
}
