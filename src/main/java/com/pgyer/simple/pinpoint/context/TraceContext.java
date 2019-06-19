package com.pgyer.simple.pinpoint.context;

import com.pgyer.simple.pinpoint.interfac.Trace;

public interface TraceContext {

    Trace currentTraceObject();

    Trace newTraceObject();

}
