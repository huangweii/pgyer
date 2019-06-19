package com.pgyer.simple.pinpoint.transformer.demo.interceptor;

import com.pgyer.simple.pinpoint.context.TraceContext;
import com.pgyer.simple.pinpoint.interfac.Trace;
import com.pgyer.simple.pinpoint.interceptor.ByteCodeMethodDescriptorSupport;
import com.pgyer.simple.pinpoint.interceptor.TraceContextSupport;
import com.pgyer.simple.pinpoint.tracevalue.MethodDescriptor;
import com.pgyer.simple.pinpoint.interceptor.SimpleAroundInterceptor;

public class RunAInterceptor implements SimpleAroundInterceptor, ByteCodeMethodDescriptorSupport, TraceContextSupport {
    private MethodDescriptor descriptor;

    private TraceContext traceContext;

    @Override
    public void before(Object target, Object[] args) {

        Trace trace = traceContext.currentTraceObject();
        if (null == trace) {
            System.out.println(target.getClass().getName());
            trace = traceContext.newTraceObject();
        }

        trace.traceBlockBegin();
        trace.markBeforeTime();
        System.out.println(Thread.currentThread().getId());
        trace.markBeforeMemoryAndCpu(Thread.currentThread().getId());
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        Trace trace = traceContext.currentTraceObject();
        if (null == trace) {
            return;
        }
        try {
            trace.recordApi(descriptor);
            trace.markAfterTime();
            trace.markAfterMemoryAndCpu(Thread.currentThread().getId());
        } finally {
            trace.traceBlockEnd();
        }
    }

    @Override
    public void setMethodDescriptor(MethodDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public void setTraceContext(TraceContext traceContext) {
        this.traceContext = traceContext;
    }
}