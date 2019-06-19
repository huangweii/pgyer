package com.pgyer.simple.pinpoint.instrument.javaassist;

import com.pgyer.simple.pinpoint.instrument.ByteCodeInstrumentor;
import com.pgyer.simple.pinpoint.instrument.InstrumentClass;
import com.pgyer.simple.pinpoint.instrument.InstrumentException;
import com.pgyer.simple.pinpoint.interfac.Agent;
import com.pgyer.simple.pinpoint.pool.NamedClassPool;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import java.net.URL;
import java.net.URLClassLoader;

public class JavaAssistByteCodeInstrumentor implements ByteCodeInstrumentor {
    private final NamedClassPool rootClassPool;
    private final NamedClassPool childClassPool;

    private Agent agent;

    public JavaAssistByteCodeInstrumentor(Agent agent) {
        this.rootClassPool = createClassPool(null, "rootClassPool");
        this.childClassPool = createChildClassPool(rootClassPool, "childClassPool");
        this.agent=agent;

    }

    public Agent getAgent() {
        return agent;
    }

    private NamedClassPool createClassPool(String[] pathNames, String classPoolName) {
        NamedClassPool classPool = new NamedClassPool(null, classPoolName);
        classPool.appendSystemPath();
//        if (pathNames != null) {
//            for (String path : pathNames) {
//                appendClassPath(classPool, path);
//            }
//        }
        //classPool.appendClassPath(new LoaderClassPath(this.getClass().getClassLoader()));
        return classPool;
    }

    private NamedClassPool createChildClassPool(ClassPool rootClassPool, String classPoolName) {
        NamedClassPool childClassPool = new NamedClassPool(rootClassPool, classPoolName);
        childClassPool.appendSystemPath();
        childClassPool.childFirstLookup = true;
        return childClassPool;
    }

    @Override
    public InstrumentClass getClass(ClassLoader classLoader, String javassistClassName, byte[] classFileBuffer) throws InstrumentException {
        // for asm : classFileBuffer
        final NamedClassPool classPool = findClassPool(classLoader);
        checkLibrary(classLoader, classPool, javassistClassName);
        try {
            CtClass cc = classPool.get(javassistClassName);
            return new JavaAssistClass(this, cc);
        } catch (NotFoundException e) {
            System.out.println("JavaAssistByteCodeInstrumentor getClass not found error");
            e.printStackTrace();
        }
        return null;
    }

    private NamedClassPool findClassPool(ClassLoader classLoader) {
        if (classLoader == null) {
            //System.out.println("classLoader is null");
        }

        return childClassPool;
    }

    public void checkLibrary(ClassLoader classLoader, NamedClassPool classPool, String javassistClassName) {
        // if it's loaded by boot class loader, classLoader is null.
        if (classLoader == null) {
            return;
        }

        // synchronized ??
//        synchronized (classPool) {
        final boolean findClass = findClass(javassistClassName, classPool);
        if (findClass) {
            //System.out.println("finded");
            return;
        }
        loadClassLoaderLibraries(classLoader, classPool);
//        }
    }

    public boolean findClass(String javassistClassName, ClassPool classPool) {
        URL url = classPool.find(javassistClassName);
        if (url == null) {
            return false;
        }
        return true;
    }

    private void loadClassLoaderLibraries(ClassLoader classLoader, NamedClassPool classPool) {
        if (classLoader instanceof URLClassLoader) {
            URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
            URL[] urlList = urlClassLoader.getURLs();
            if (urlList != null) {
                final String classLoaderName = classLoader.getClass().getName();
                final String classPoolName = classPool.getName();
                for (URL tempURL : urlList) {
                    String filePath = tempURL.getFile();
                    System.out.println("classLoader name" + classLoaderName + " classPool" + classPoolName + "filepath" + filePath);
                }
            }
        }
        classPool.appendClassPath(new LoaderClassPath(classLoader));
    }
}
