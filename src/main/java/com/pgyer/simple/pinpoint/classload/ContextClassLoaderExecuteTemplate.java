package com.pgyer.simple.pinpoint.classload;

import java.util.concurrent.Callable;

public class ContextClassLoaderExecuteTemplate<V> {
    private final ClassLoader classLoader;

    public ContextClassLoaderExecuteTemplate(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new NullPointerException("classLoader must not be null");
        }
        this.classLoader = classLoader;
    }

    public V execute(Callable<V> callable) {
        try {
            final Thread currentThread = Thread.currentThread();
            final ClassLoader before = currentThread.getContextClassLoader();
            currentThread.setContextClassLoader(ContextClassLoaderExecuteTemplate.this.classLoader);
            try {
                return callable.call();
            } finally {
                // even though  the "before" classloader  is null, rollback  is needed.
                // if an exception occurs before callable.call(), the call flow can't reach here.
                // so  rollback  here is right.
                currentThread.setContextClassLoader(before);
            }
        } catch (Exception ex) {
            System.out.println("execute fail. Caused:" + ex.getMessage());
            return null;
        }
    }
}
