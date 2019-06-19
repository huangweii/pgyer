package com.pgyer.simple.pinpoint.trans.filter;

import java.security.ProtectionDomain;

public interface ClassFileFilter {
    public static final boolean SKIP = true;
    public static final boolean CONTINUE = false;

    boolean doFilter(ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer);
}
