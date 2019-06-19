package com.pgyer.simple.pinpoint.context;

import com.pgyer.simple.pinpoint.interfac.Trace;

public class DefaultTraceContext implements TraceContext{
    private final TraceFactory traceFactory;

    public DefaultTraceContext(){
        this.traceFactory = new ThreadLocalTraceFactory(this);
    }

    public Trace currentTraceObject() {
        return traceFactory.currentTraceObject();
    }

    @Override
    public Trace newTraceObject() {
        return traceFactory.newTraceObject();
    }
}
