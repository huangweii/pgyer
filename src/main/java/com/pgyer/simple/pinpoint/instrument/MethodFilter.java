package com.pgyer.simple.pinpoint.instrument;

public interface MethodFilter {
    boolean filter(MethodInfo method);
}
