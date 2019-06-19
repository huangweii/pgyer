package com.pgyer.simple.pinpoint.interceptor;
import com.pgyer.simple.pinpoint.trans.transformer.WeakAtomicReferenceArray;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InterceptorRegistry {
    public static final InterceptorRegistry REGISTRY = new InterceptorRegistry();
    private final int registrySize;
    private final static int DEFAULT_MAX = 4096;

    private final AtomicInteger id = new AtomicInteger(0);
    private final WeakAtomicReferenceArray<SimpleAroundInterceptor> simpleIndex;
    public InterceptorRegistry() {
        this(DEFAULT_MAX);
    }

    InterceptorRegistry(int maxRegistrySize) {
        if (maxRegistrySize < 0) {
            throw new IllegalArgumentException("negative maxRegistrySize:" + maxRegistrySize);
        }
        this.registrySize = maxRegistrySize;
        this.simpleIndex = new WeakAtomicReferenceArray<SimpleAroundInterceptor>(maxRegistrySize, SimpleAroundInterceptor.class);
    }

    public static int addSimpleInterceptor(SimpleAroundInterceptor interceptor) {
        return REGISTRY.addSimpleInterceptor0(interceptor);
    }

    public static SimpleAroundInterceptor getSimpleInterceptor(int key) {
        return REGISTRY.getSimpleInterceptor0(key);
    }

    SimpleAroundInterceptor getSimpleInterceptor0(int key) {
        final SimpleAroundInterceptor interceptor = simpleIndex.get(key);
        if (interceptor == null) {
            //return LOGGING_INTERCEPTOR upon wrong logic
            throw new IllegalArgumentException("key:" + key);
        }
        return interceptor;
    }


    int addSimpleInterceptor0(SimpleAroundInterceptor interceptor) {
        if (interceptor == null) {
            return -1;
        }
        final int newId = nextId();
        if (newId >= registrySize) {
            throw new IndexOutOfBoundsException("size=" + simpleIndex.length() + " id=" + id);
        }

        this.simpleIndex.set(newId, interceptor);
        return newId;
    }

    private int nextId() {
        return id.getAndIncrement();
    }

    public static Interceptor findInterceptor(int key) {
        return REGISTRY.findInterceptor0(key);
    }

    public Interceptor findInterceptor0(int key) {
        final SimpleAroundInterceptor simpleInterceptor = this.simpleIndex.get(key);
        if (simpleInterceptor != null) {
            return simpleInterceptor;
        }
//        final StaticAroundInterceptor staticAroundInterceptor = this.staticIndex.get(key);
//        if (staticAroundInterceptor != null) {
//            return staticAroundInterceptor;
//        }

        return null;
    }
}
