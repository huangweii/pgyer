package com.pgyer.simple.pinpoint.interfac;

import com.pgyer.simple.pinpoint.context.TraceContext;

public interface Agent {
    void start();

    void stop();

    TraceContext getTraceContext();
}
