package com.pgyer.simple.pinpoint.classload;

import java.net.URL;
import java.net.URLClassLoader;

public class PinpointURLClassLoader extends URLClassLoader {
    private final ClassLoader parent;

    private final ProfilerLibClass profilerLibClass = new ProfilerLibClass();

    public PinpointURLClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        if (parent == null) {
            //throw new NullPointerException("parent must not be null");
            this.parent = new URLClassLoader(urls);
        }else {
            this.parent = parent;
        }
    }


    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // First, check if the class has already been loaded
        //System.out.println(name);
        Class clazz = findLoadedClass(name);
        if (clazz == null) {
            if (onLoadClass(name)) {
                // load a class used for Pinpoint itself by this PinpointURLClassLoader
                clazz = findClass(name);
            } else {
                try {
                    //System.out.println("1");
                    //System.out.println(parent);
                    // load a class by parent ClassLoader
                    clazz = parent.loadClass(name);
                } catch (ClassNotFoundException ignore) {
                }
                if (clazz == null) {
                    // if not found, try to load a class by this PinpointURLClassLoader
                    clazz = findClass(name);
                }
            }
        }
        if (resolve) {
            resolveClass(clazz);
        }
        return clazz;
    }

    boolean onLoadClass(String name) {
        return profilerLibClass.onLoadClass(name);
    }
}
