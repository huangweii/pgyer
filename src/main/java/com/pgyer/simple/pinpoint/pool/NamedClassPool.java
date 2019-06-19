package com.pgyer.simple.pinpoint.pool;

import javassist.ClassPool;

public class NamedClassPool extends ClassPool{
    private final String name;

    public NamedClassPool(String name) {
        this.name = name;
    }

    public NamedClassPool(boolean useDefaultPath, String name) {
        super(useDefaultPath);
        this.name = name;
    }

    public NamedClassPool(ClassPool parent, String name) {
        super(parent);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
