package com.pgyer.simple.pinpoint.monitor.metric;

import com.pgyer.simple.pinpoint.monitor.metric.cpu.CpuLoadMetricSnapshot;

public class AgentStatMetricSnapshot {
    private JvmGcMetricSnapshot gc;
    private CpuLoadMetricSnapshot cpuLoad;

    public JvmGcMetricSnapshot getGc() {
        return gc;
    }

    public void setGc(JvmGcMetricSnapshot gc) {
        this.gc = gc;
    }

    public CpuLoadMetricSnapshot getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(CpuLoadMetricSnapshot cpuLoad) {
        this.cpuLoad = cpuLoad;
    }
}
