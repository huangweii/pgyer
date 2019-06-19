package com.pgyer.simple.pinpoint.instrument;

public interface ByteCodeInstrumentor {
    InstrumentClass getClass(ClassLoader classLoader, String jvmClassName, byte[] classFileBuffer) throws InstrumentException;
}
