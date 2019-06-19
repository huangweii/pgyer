package com.pgyer.simple.pinpoint.interceptor;

import com.pgyer.simple.pinpoint.context.TraceContext;
import com.pgyer.simple.pinpoint.tracevalue.MethodDescriptor;

public interface SimpleAroundInterceptor extends Interceptor{

    void before(Object target, Object[] args);

    void after(Object target, Object[] args, Object result, Throwable throwable);
}
