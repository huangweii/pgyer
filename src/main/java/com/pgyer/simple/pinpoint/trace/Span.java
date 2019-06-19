package com.pgyer.simple.pinpoint.trace;

import com.pgyer.simple.pinpoint.monitor.DefaultAgentStatMonitor;
import com.pgyer.simple.pinpoint.monitor.metric.AgentStatMetricSnapshot;
import com.pgyer.simple.pinpoint.monitor.metric.JvmGcMetricSnapshot;
import com.pgyer.simple.pinpoint.monitor.metric.cpu.CpuLoadMetricSnapshot;
import com.pgyer.simple.pinpoint.tracevalue.MethodDescriptor;

public class Span {
    private String agentId;
    private long agentStartTime;

    private String transactionId;
    private long spanId;
    private long parentSpanId;
    private short flag;

    private long startElapsed;
    private long endElapsed;

    private boolean isSetStartElapsed;

    private MethodDescriptor descriptor;

    private long threadID;
    private String threadName;

    private int depthId;

    private AgentStatMetricSnapshot startMemoryAndCpu;
    private AgentStatMetricSnapshot endMemoryAndCpu;
    private AgentStatMetricSnapshot usedMemoryAndCpu;

    public long getThreadID() {
        return threadID;
    }

    public void setThreadID(long threadID) {
        this.threadID = threadID;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public Span(String transactionId, long parentSpanId, long spanId, int depthId) {
        this.transactionId = transactionId;
        this.parentSpanId = parentSpanId;
        this.spanId = spanId;
        this.depthId = depthId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public void setAgentStartTime(long agentStartTime) {
        this.agentStartTime = agentStartTime;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public long getSpanId() {
        return this.spanId;
    }

    public long getParentSpanId() {
        return this.parentSpanId;
    }

    public void setStartElapsed() {
        this.startElapsed = System.nanoTime();
        this.isSetStartElapsed = true;
    }

    public void setEndElapsed() {
        if (!isSetStartElapsed) {
            throw new IllegalArgumentException("startTime is not set");
        }
        this.endElapsed = System.nanoTime() - getStartElapsed();
    }

    public long getStartElapsed() {
        return this.startElapsed;
    }

    public long getEndElapsed() {
        return this.endElapsed;
    }

    public void setDescriptor(MethodDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public MethodDescriptor getDescriptor() {
        return this.descriptor;
    }

    public int getDepthId() {
        return depthId;
    }


    public void setStartMemoryAndCpu(long id) {
        this.startMemoryAndCpu = DefaultAgentStatMonitor.getInstance().start(id);
    }

    public void setEndMemoryAndCpu(long id) {
        this.endMemoryAndCpu = DefaultAgentStatMonitor.getInstance().start(id);

        JvmGcMetricSnapshot jvmGcMetricSnapshot = new JvmGcMetricSnapshot();
        jvmGcMetricSnapshot.setType(this.endMemoryAndCpu.getGc().getType());
        jvmGcMetricSnapshot.setJvmMemoryHeapUsed(this.endMemoryAndCpu.getGc().getJvmMemoryHeapUsed() - this.startMemoryAndCpu.getGc().getJvmMemoryHeapUsed());
        jvmGcMetricSnapshot.setJvmMemoryNonHeapUsed(this.endMemoryAndCpu.getGc().getJvmMemoryNonHeapUsed() - this.startMemoryAndCpu.getGc().getJvmMemoryNonHeapUsed());

        CpuLoadMetricSnapshot cpuLoadMetricSnapshot = new CpuLoadMetricSnapshot();
        //cpuLoadMetricSnapshot.setJvmCpuUsage(this.endMemoryAndCpu.getCpuLoad().getJvmCpuUsage() - this.startMemoryAndCpu.getCpuLoad().getJvmCpuUsage());
        //cpuLoadMetricSnapshot.setSystemCpuUsage(this.endMemoryAndCpu.getCpuLoad().getSystemCpuUsage() - this.startMemoryAndCpu.getCpuLoad().getSystemCpuUsage());
        cpuLoadMetricSnapshot.setJvmCpuTime(this.endMemoryAndCpu.getCpuLoad().getJvmCpuTime() - this.startMemoryAndCpu.getCpuLoad().getJvmCpuTime());
        cpuLoadMetricSnapshot.setJvmUserCpuTime(this.endMemoryAndCpu.getCpuLoad().getJvmUserCpuTime()-this.startMemoryAndCpu.getCpuLoad().getJvmUserCpuTime());

        this.usedMemoryAndCpu = new AgentStatMetricSnapshot();
        usedMemoryAndCpu.setGc(jvmGcMetricSnapshot);
        usedMemoryAndCpu.setCpuLoad(cpuLoadMetricSnapshot);
    }

    public AgentStatMetricSnapshot getStartMemoryAndCpu() {
        return this.startMemoryAndCpu;
    }

    public AgentStatMetricSnapshot getEndMemoryAndCpu() {
        return this.endMemoryAndCpu;
    }

    public AgentStatMetricSnapshot getUsedMemoryAndCpu() {
        return this.usedMemoryAndCpu;
    }
}
