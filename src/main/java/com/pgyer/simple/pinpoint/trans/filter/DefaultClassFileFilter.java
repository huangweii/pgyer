package com.pgyer.simple.pinpoint.trans.filter;

import java.security.ProtectionDomain;

public class DefaultClassFileFilter implements ClassFileFilter {
    private final ClassLoader agentLoader;

    public DefaultClassFileFilter(ClassLoader agentLoader) {
        if (agentLoader == null) {
            //throw new NullPointerException("agentLoader must not be null");
        }
        this.agentLoader = agentLoader;
    }

    @Override
    public boolean doFilter(ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) {
//        if (!className.startsWith("com/first")){
//            return SKIP;
//        }
        // skip java classes
        if (className.startsWith("java")||className.startsWith("jdk")||className.startsWith("sun")||className.startsWith("org")||className.startsWith("com/sun")
        ||className.startsWith("ch")||className.startsWith("com/mchange")||className.startsWith("com/fasterxml")||className.startsWith("com/mysql")) {
            return SKIP;
        }

        if (classLoader == null) {
            // skip classes loaded by agent class loader.
            return SKIP;
        }

        // Skip pinpoint packages too.
        if (className.startsWith("com/pgyer/simple/pinpoint/")) {
            return SKIP;
        }
        return CONTINUE;
    }
}
