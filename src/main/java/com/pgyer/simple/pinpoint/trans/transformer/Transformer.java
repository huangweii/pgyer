package com.pgyer.simple.pinpoint.trans.transformer;

import java.security.ProtectionDomain;

public interface Transformer {
    byte[] modify(ClassLoader classLoader, String className, ProtectionDomain protectedDomain, byte[] classFileBuffer);
}
