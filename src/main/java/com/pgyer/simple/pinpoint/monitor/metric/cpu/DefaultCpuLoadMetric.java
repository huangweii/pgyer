package com.pgyer.simple.pinpoint.monitor.metric.cpu;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class DefaultCpuLoadMetric implements CpuLoadMetric {
    //private OperatingSystemMXBean operatingSystemMXBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    private ThreadMXBean mx = ManagementFactory.getThreadMXBean();

    public DefaultCpuLoadMetric() {
    }

    @Override
    public CpuLoadMetricSnapshot getSnapshot(long id) {
        //double jvmCpuUsage = operatingSystemMXBean.getProcessCpuLoad();
        //double systemCpuUsage = operatingSystemMXBean.getSystemCpuLoad();
        long jvmCpuTime = 0;
        long jvmUserCpuTime = 0;
        for (long idTemp : mx.getAllThreadIds()) {
            if (id == idTemp) {
                //System.out.println("! "+id);
                jvmCpuTime = mx.getThreadCpuTime(id);
                jvmUserCpuTime = mx.getThreadUserTime(id);
            }
        }
        return new CpuLoadMetricSnapshot(jvmCpuTime, jvmUserCpuTime);
    }
}
