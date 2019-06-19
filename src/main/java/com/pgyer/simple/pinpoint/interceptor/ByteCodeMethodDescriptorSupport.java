package com.pgyer.simple.pinpoint.interceptor;

import com.pgyer.simple.pinpoint.tracevalue.MethodDescriptor;

public interface ByteCodeMethodDescriptorSupport {
    void setMethodDescriptor(MethodDescriptor descriptor);
}
