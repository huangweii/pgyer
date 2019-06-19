package com.pgyer.simple.pinpoint.trace;

import com.pgyer.simple.pinpoint.context.TraceContext;
import com.pgyer.simple.pinpoint.interfac.Trace;
import com.pgyer.simple.pinpoint.interfac.TraceId;
import com.pgyer.simple.pinpoint.tracevalue.MethodDescriptor;
import com.pgyer.simple.pinpoint.tree.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class DefaultTrace implements Trace {

    private final TraceContext traceContext;

    private TraceId traceId;

    private List<Span> spans = new ArrayList<Span>();

    private TreeNode treeNode;

    private boolean flag = false;

    public DefaultTrace(final TraceContext traceContext, long transactionId) {
        if (traceContext == null) {
            throw new NullPointerException("traceContext must not be null");
        }
        this.traceContext = traceContext;
        this.traceId = new DefaultTraceId(transactionId);
        this.treeNode = new TreeNode();

    }

    private Span createSpan(final TraceId traceId) {
        final Span span = new Span(traceId.getTransactionId(), traceId.getParentSpanId(), traceId.getSpanId(), traceId.getDepthId());
        span.setAgentId(traceId.getAgentId());
        span.setAgentStartTime(traceId.getAgentStartTime());

        return span;
    }

    @Override
    public void traceBlockBegin() {
        traceBlockBegin(DEFAULT_STACKID);
    }

    @Override
    public void traceBlockBegin(final int stackId) {
        final Span span = createSpan(traceId);
        spans.add(span);
        this.traceId = this.traceId.getNextTraceId();
    }

    @Override
    public void traceBlockEnd() {
        traceBlockEnd(DEFAULT_STACKID);
    }

    @Override
    public void traceBlockEnd(int stackId) {
        Span span = spans.get(spans.size() - 1);

        int depthId = span.getDepthId();
        if (depthId > 0) {
            this.traceId.setDepthId(depthId);
        }

        treeNode.addSpan(span);
        spans.remove(spans.size() - 1);

        if (spans.size() == 0) {
            if (!flag) {
                treeNode.call();
                flag = true;
            }
        }
    }

    @Override
    public void markBeforeTime() {
        this.spans.get(spans.size() - 1).setStartElapsed();
    }

    @Override
    public void markAfterTime() {
        this.spans.get(spans.size() - 1).setEndElapsed();
    }

    @Override
    public long getBeforeTime() {
        return this.spans.get(0).getStartElapsed();
    }

    @Override
    public long getAfterTime() {
        return this.spans.get(0).getEndElapsed();
    }

    @Override
    public void recordApi(MethodDescriptor methodDescriptor) {
        if (methodDescriptor == null) {
            return;
        }
        this.spans.get(spans.size() - 1).setDescriptor(methodDescriptor);
    }

    @Override
    public void markBeforeMemoryAndCpu(long id) {
        this.spans.get(spans.size() - 1).setStartMemoryAndCpu(id);
    }

    @Override
    public void markAfterMemoryAndCpu(long id) {
        this.spans.get(spans.size() - 1).setEndMemoryAndCpu(id);
    }

    @Override
    public void markThreadId(long id) {
        this.spans.get(spans.size() - 1).setThreadID(id);
    }

    @Override
    public void markThreadName(String name) {
        this.spans.get(spans.size() - 1).setThreadName(name);
    }


}