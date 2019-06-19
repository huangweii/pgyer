package com.pgyer.simple.pinpoint.transformer.httpurlconnection.interceptor;

import com.pgyer.simple.pinpoint.context.TraceContext;
import com.pgyer.simple.pinpoint.interfac.Trace;
import com.pgyer.simple.pinpoint.tracevalue.MethodDescriptor;
import com.pgyer.simple.pinpoint.interceptor.SimpleAroundInterceptor;

public class MethodTwoInterceptor implements SimpleAroundInterceptor {

    private MethodDescriptor descriptor;

    private TraceContext traceContext;

    @Override
    public void before(Object target, Object[] args) {

        Trace trace=traceContext.currentTraceObject();
        if (null==trace){
           return;
        }

        trace.traceBlockBegin();
        trace.markBeforeTime();

    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {

        Trace trace=traceContext.currentTraceObject();
        if (null==trace){
            return;
        }
        try{
            trace.recordApi(descriptor);
            trace.markAfterTime();
        }finally {
            trace.traceBlockEnd();
        }
    }

    public void setMethodDescriptor(MethodDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public void setTraceContext(TraceContext traceContext) {
        this.traceContext = traceContext;
    }
}
