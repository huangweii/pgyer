package com.pgyer.simple.pinpoint.monitor.metric;

import com.pgyer.simple.pinpoint.monitor.metric.gc.JvmGcType;

public class JvmGcMetricSnapshot {
    private JvmGcType type;
    private long jvmMemoryHeapUsed;
    private long jvmMemoryHeapMax;
    private long jvmMemoryNonHeapUsed;
    private long jvmMemoryNonHeapMax;

    public JvmGcType getType() {
        return type;
    }

    public void setType(JvmGcType type) {
        this.type = type;
    }

    public long getJvmMemoryHeapUsed() {
        return jvmMemoryHeapUsed;
    }

    public void setJvmMemoryHeapUsed(long jvmMemoryHeapUsed) {
        this.jvmMemoryHeapUsed = jvmMemoryHeapUsed;
    }

    public long getJvmMemoryHeapMax() {
        return jvmMemoryHeapMax;
    }

    public void setJvmMemoryHeapMax(long jvmMemoryHeapMax) {
        this.jvmMemoryHeapMax = jvmMemoryHeapMax;
    }

    public long getJvmMemoryNonHeapUsed() {
        return jvmMemoryNonHeapUsed;
    }

    public void setJvmMemoryNonHeapUsed(long jvmMemoryNonHeapUsed) {
        this.jvmMemoryNonHeapUsed = jvmMemoryNonHeapUsed;
    }

    public long getJvmMemoryNonHeapMax() {
        return jvmMemoryNonHeapMax;
    }

    public void setJvmMemoryNonHeapMax(long jvmMemoryNonHeapMax) {
        this.jvmMemoryNonHeapMax = jvmMemoryNonHeapMax;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        //sb.append("type=").append(type);
        sb.append("heapUsed=").append(jvmMemoryHeapUsed);
        sb.append(",heapMax=").append(jvmMemoryHeapMax);
        sb.append(",nonHeapUsed=").append(jvmMemoryNonHeapUsed);
        sb.append(",nonHeapMax=").append(jvmMemoryNonHeapMax);
        //sb.append('}');
        return sb.toString();
    }
}
