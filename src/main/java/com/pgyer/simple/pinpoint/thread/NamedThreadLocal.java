package com.pgyer.simple.pinpoint.thread;

public class NamedThreadLocal<T> extends ThreadLocal<T> {
    private final String name;

    public NamedThreadLocal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
