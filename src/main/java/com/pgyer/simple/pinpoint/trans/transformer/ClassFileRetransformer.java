package com.pgyer.simple.pinpoint.trans.transformer;

import com.pgyer.simple.pinpoint.trans.transformer.AbstractClassFileTransformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import java.util.concurrent.ConcurrentHashMap;

public class ClassFileRetransformer implements ClassFileTransformer {

    private final Instrumentation instrumentation;
    private final ConcurrentHashMap<Class<?>, AbstractClassFileTransformer> targets = new ConcurrentHashMap<Class<?>, AbstractClassFileTransformer>();

    public ClassFileRetransformer(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (classBeingRedefined == null) {
            return null;
        }
        final AbstractClassFileTransformer modifier = targets.remove(classBeingRedefined);
        if (modifier == null) {
            return null;
        }

        return modifier.modify(loader, className.replace('/', '.'), protectionDomain, classfileBuffer);
    }

    public void retransform(Class<?> target, AbstractClassFileTransformer modifier) {
        AbstractClassFileTransformer removed = targets.put(target, modifier);

        if (removed != null) {
            // TODO log
        }

        try {
            instrumentation.retransformClasses(target);
        } catch (UnmodifiableClassException e) {
            System.out.println("ClassFileRetransformer retransform exception");
        }
    }
}
