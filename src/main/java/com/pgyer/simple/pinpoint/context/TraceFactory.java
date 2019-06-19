package com.pgyer.simple.pinpoint.context;

import com.pgyer.simple.pinpoint.interfac.Trace;

public interface TraceFactory {
    Trace currentTraceObject();
    Trace newTraceObject();

}
