package com.pgyer.simple.pinpoint.interfac;

import com.pgyer.simple.pinpoint.tracevalue.MethodDescriptor;

public interface RecordableTrace {
    void markBeforeTime();

    long getBeforeTime();

    void markAfterTime();

    long getAfterTime();

    void markThreadId(long id);

    void markThreadName(String name);

//    TraceId getTraceId();
//
//    boolean canSampled();
//
//    boolean isRoot();
//
    void recordApi(MethodDescriptor methodDescriptor);
//
//    void recordNextSpanId(long spanId);
//
//    void recordEndPoint(String endPoint);
//
    void markBeforeMemoryAndCpu(long id);

    void markAfterMemoryAndCpu(long id);


}
