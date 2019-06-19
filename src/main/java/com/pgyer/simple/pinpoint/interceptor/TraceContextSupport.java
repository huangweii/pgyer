package com.pgyer.simple.pinpoint.interceptor;

import com.pgyer.simple.pinpoint.context.TraceContext;

public interface TraceContextSupport {
    void setTraceContext(TraceContext traceContext);

}
