package com.pgyer.simple.pinpoint;

import com.pgyer.simple.pinpoint.classload.AgentClassLoader;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public class PinpointAgent {
    private static final String path = "/Users/pgyer/Desktop/simplepinpoint/target/simplepinpoint-1.0-SNAPSHOT.jar";

    public static final String BOOT_CLASS = "com.pgyer.simple.pinpoint.DefaultAgent";

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("premain was called.");
//        for (Class name : instrumentation.getAllLoadedClasses()) {
//            System.out.println("*" + name.getName());
//        }

//        ClassFileTransformer trans = new AgentTransformer(instrumentation);
//        instrumentation.addTransformer(trans);
        //JarFile jarFile = getBootStrapJarFile(path);
        //instrumentation.appendToBootstrapClassLoaderSearch(jarFile);

        new DefaultAgent(instrumentation);
//        try {
//            List<URL> libUrlList = resolveLib();
//            AgentClassLoader agentClassLoader = new AgentClassLoader(libUrlList.toArray(new URL[libUrlList.size()]));
//            agentClassLoader.setBootClass(BOOT_CLASS);
//            //System.out.println("agent start");
//            agentClassLoader.boot(instrumentation);
//            //System.out.println("agent started normally.");
//        }catch (Throwable e){
//            System.out.println("q");
//        }


    }

//    public static void agentmain(String agentArgs, Instrumentation instrumentation) throws UnmodifiableClassException {
//        System.out.println("agentmain 启动成功");
//
//        new DefaultAgent(instrumentation);
//
//
//
//
//    }


    private static JarFile getBootStrapJarFile(String bootStrapCoreJar) {
        try {
            return new JarFile(bootStrapCoreJar);
        } catch (IOException ioe) {
            System.out.println("ioe exception");
            return null;
        }
    }

    private static List<URL> resolveLib() {
        final List<URL> jarURLList = new ArrayList<URL>();
        File file = new File(path);
        URL url = toURI(file);
        jarURLList.add(url);
        return jarURLList;
    }

    private static URL toURI(File file) {
        URI uri = file.toURI();
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            System.out.println("toURL exception");
            return null;
        }
    }
}

