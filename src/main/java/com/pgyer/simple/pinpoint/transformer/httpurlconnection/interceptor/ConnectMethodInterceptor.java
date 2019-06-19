package com.pgyer.simple.pinpoint.transformer.httpurlconnection.interceptor;

import com.pgyer.simple.pinpoint.context.TraceContext;
import com.pgyer.simple.pinpoint.interfac.Trace;
import com.pgyer.simple.pinpoint.tracevalue.MethodDescriptor;
import com.pgyer.simple.pinpoint.interceptor.SimpleAroundInterceptor;

public class ConnectMethodInterceptor implements SimpleAroundInterceptor {

    private MethodDescriptor descriptor;

    private TraceContext traceContext;

    public ConnectMethodInterceptor(){}

    @Override
    public void before(Object target, Object[] args) {
        //System.out.println("ConnectMethodInterceptor.before in");

        Trace trace=traceContext.currentTraceObject();

        if (null==trace){
            trace=traceContext.newTraceObject();
        }

        trace.traceBlockBegin();
        trace.markBeforeTime();


    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        //System.out.println("ConnectMethodInterceptor.after in");
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
