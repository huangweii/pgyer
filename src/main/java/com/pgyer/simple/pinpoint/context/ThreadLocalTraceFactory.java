package com.pgyer.simple.pinpoint.context;

import com.pgyer.simple.pinpoint.interfac.Trace;
import com.pgyer.simple.pinpoint.thread.NamedThreadLocal;
import com.pgyer.simple.pinpoint.trace.DefaultTrace;

import java.util.concurrent.atomic.AtomicLong;

public class ThreadLocalTraceFactory implements TraceFactory{
    private final TraceContext traceContext;

    private final NamedThreadLocal<Trace> threadLocal = new NamedThreadLocal<Trace>("Trace");

    private final AtomicLong transactionId = new AtomicLong(0);

    public ThreadLocalTraceFactory(TraceContext traceContext) {
        this.traceContext = traceContext;
    }

    public Trace currentTraceObject() {
        //System.out.println(threadLocal.get());
        return threadLocal.get();
    }

    public Trace newTraceObject() {
        checkBeforeTraceObject();
        final DefaultTrace trace = new DefaultTrace(traceContext, nextTransactionId());
        threadLocal.set(trace);
        //System.out.println(threadLocal.get());
        return trace;
    }

    private void checkBeforeTraceObject() {
        final Trace old = this.threadLocal.get();
        if (old != null) {
            throw new IllegalArgumentException("already Trace Object exist.");
        }
    }

    private long nextTransactionId() {
        return this.transactionId.getAndIncrement();
    }
}
