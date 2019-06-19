package com.pgyer.simple.pinpoint.instrument;

import com.pgyer.simple.pinpoint.interceptor.Interceptor;

import java.util.List;

public interface InstrumentClass {
    String getName();
    boolean isInterceptable();

    List<MethodInfo> getDeclaredMethods();

    int addInterceptor(String methodName, String[] args, Interceptor interceptor) throws InstrumentException;

    byte[] toBytecode() throws InstrumentException;
}
