package com.pgyer.simple.pinpoint.classload;

import com.pgyer.simple.pinpoint.PinpointAgent;
import com.pgyer.simple.pinpoint.interfac.Agent;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.Callable;

public class AgentClassLoader {
    private static final SecurityManager SECURITY_MANAGER = System.getSecurityManager();
    private final URLClassLoader classLoader;

    private String bootClass;

    private Agent agentBootStrap;
    private final ContextClassLoaderExecuteTemplate<Object> executeTemplate;

    public AgentClassLoader(URL[] urls) {
        if (urls == null) {
            throw new NullPointerException("urls");
        }

        ClassLoader bootStrapClassLoader = AgentClassLoader.class.getClassLoader();
        //System.out.println("bootStrapClassLoader is"+bootStrapClassLoader);
        ClassLoader pinClassLoader = PinpointAgent.class.getClassLoader();
        //System.out.println("bootStrapClassLoader is"+pinClassLoader);
        this.classLoader = createClassLoader(urls, bootStrapClassLoader);

        this.executeTemplate = new ContextClassLoaderExecuteTemplate<Object>(classLoader);
    }

    private PinpointURLClassLoader createClassLoader(final URL[] urls, final ClassLoader bootStrapClassLoader) {
        if (SECURITY_MANAGER != null) {
            return AccessController.doPrivileged(new PrivilegedAction<PinpointURLClassLoader>() {
                public PinpointURLClassLoader run() {
                    return new PinpointURLClassLoader(urls, bootStrapClassLoader);
                }
            });
        } else {
            return new PinpointURLClassLoader(urls, bootStrapClassLoader);
        }
    }

    public void setBootClass(String bootClass) {
        this.bootClass = bootClass;
    }

    public void boot(final Instrumentation instrumentation) {
        final Class<?> bootStrapClazz = getBootStrapClass();

        final Object agent = executeTemplate.execute(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    Constructor<?> constructor = bootStrapClazz.getConstructor(Instrumentation.class);
                    return constructor.newInstance(instrumentation);
                } catch (InstantiationException e) {
                    System.out.println("boot create failed. Error:"+e.getMessage());
                    return null;
                } catch (IllegalAccessException e) {
                    System.out.println("boot method invoke failed. Error:" + e.getMessage());
                    return null;
                }
            }
        });

        if (agent instanceof Agent) {
            this.agentBootStrap = (Agent) agent;
        } else {
            String agentClassName;
            if (agent == null) {
                agentClassName = "Agent is null";
            } else {
                agentClassName = agent.getClass().getName();
            }
            System.out.println("Invalid AgentType. boot failed. AgentClass:" + agentClassName);
        }
    }

    private Class<?> getBootStrapClass() {
        try {
            return this.classLoader.loadClass(bootClass);
        } catch (ClassNotFoundException e) {
            System.out.println("boot class not found. bootClass:"+bootClass);
            return null;
        }
    }
}
