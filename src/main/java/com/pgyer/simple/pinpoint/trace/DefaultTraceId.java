package com.pgyer.simple.pinpoint.trace;

import com.pgyer.simple.pinpoint.interfac.TraceId;
import com.pgyer.simple.pinpoint.utils.TransactionIdUtils;

public class DefaultTraceId implements TraceId {

    private final String agentId = "pinpoint";
    private final long agentStartTime = 1l;

    private final long transactionSequence;

    private long parentSpanId;
    private final long spanId;
    private final short flags;

    private int depthId;

    public DefaultTraceId(long transactionId) {
        this(transactionId, SpanId.NULL, SpanId.newSpanId(), (short) 0,1);
    }

    public TraceId getNextTraceId() {
        return new DefaultTraceId(transactionSequence, spanId, SpanId.nextSpanID(spanId, parentSpanId), flags,depthId+1);
    }

    public DefaultTraceId(long transactionId, long parentSpanId, long spanId, short flags,int depthId) {
        this.transactionSequence = transactionId;

        this.parentSpanId = parentSpanId;
        this.spanId = spanId;
        this.flags = flags;
        this.depthId=depthId;
    }


    public String getTransactionId() {
        return TransactionIdUtils.formatString(agentId, agentStartTime, transactionSequence);
    }

    public String getAgentId() {
        return agentId;
    }

    public long getAgentStartTime() {
        return agentStartTime;
    }

    public long getTransactionSequence() {
        return transactionSequence;
    }


    public long getParentSpanId() {
        return parentSpanId;
    }

    public long getSpanId() {
        return spanId;
    }

    public short getFlags() {
        return flags;
    }

    public boolean isRoot() {
        return this.parentSpanId == SpanId.NULL;
    }

    public int getDepthId() {
        return depthId;
    }

    public void setDepthId(int depthId) {
        this.depthId=depthId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultTraceID{");
        sb.append("agentId='").append(agentId).append('\'');
        sb.append(", agentStartTime=").append(agentStartTime);
        sb.append(", transactionSequence=").append(transactionSequence);
        sb.append(", parentSpanId=").append(parentSpanId);
        sb.append(", spanId=").append(spanId);
        sb.append(", flags=").append(flags);
        sb.append('}');
        return sb.toString();
    }

}
