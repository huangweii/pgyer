package com.pgyer.simple.pinpoint.monitor.metric.gc;

public enum JvmGcType {
    UNKNOWN(0),
    SERIAL(1),
    PARALLEL(2),
    CMS(3),
    G1(4);

    private final int value;

    private JvmGcType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
