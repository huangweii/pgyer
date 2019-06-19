package com.pgyer.simple.pinpoint.interfac;

public interface TraceId {
    TraceId getNextTraceId();

    long getSpanId();

    String getTransactionId();

    String getAgentId();

    long getAgentStartTime();

    long getTransactionSequence();

    long getParentSpanId();

    short getFlags();

    boolean isRoot();

    int getDepthId();

    void setDepthId(int depthId);
}
