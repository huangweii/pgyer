package com.pgyer.simple.pinpoint.classload;

public class ProfilerLibClass {

    private static final String[] PINPOINT_PROFILER_CLASS = new String[] {
    };

    public boolean onLoadClass(String clazzName) {
        final int length = PINPOINT_PROFILER_CLASS.length;
        for (int i = 0; i < length; i++) {
            if (clazzName.startsWith(PINPOINT_PROFILER_CLASS[i])) {
                return true;
            }
        }
        return false;
    }
}
