package com.pgyer.simple.pinpoint.transformer.demo;

import com.pgyer.simple.pinpoint.instrument.MethodFilter;
import com.pgyer.simple.pinpoint.instrument.MethodInfo;

public class SkipMethodFilter implements MethodFilter {

    public static final MethodFilter FILTER = new SkipMethodFilter();

    @Override
    public boolean filter(MethodInfo ctMethod) {
        return false;
    }
}
