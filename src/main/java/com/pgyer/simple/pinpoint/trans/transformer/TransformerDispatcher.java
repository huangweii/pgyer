package com.pgyer.simple.pinpoint.trans.transformer;

import com.pgyer.simple.pinpoint.DefaultAgent;
import com.pgyer.simple.pinpoint.context.TraceContext;
import com.pgyer.simple.pinpoint.instrument.ByteCodeInstrumentor;
import com.pgyer.simple.pinpoint.trans.filter.ClassFileFilter;
import com.pgyer.simple.pinpoint.trans.filter.DefaultClassFileFilter;
import com.pgyer.simple.pinpoint.utils.JavaAssistUtils;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class TransformerDispatcher implements ClassFileTransformer {

    private TransformerRegistry transformerRegistry;

    private final ClassLoader agentClassLoader = this.getClass().getClassLoader();

    private final DefaultAgent defaultAgent;

    private final ClassFileFilter skipFilter;

    private final ByteCodeInstrumentor byteCodeInstrumentor;

    public TransformerDispatcher(DefaultAgent defaultAgent, ByteCodeInstrumentor byteCodeInstrumentor) {
        this.defaultAgent=defaultAgent;
        this.byteCodeInstrumentor=byteCodeInstrumentor;
        this.transformerRegistry = createModifierRegistry();
        this.skipFilter = new DefaultClassFileFilter(agentClassLoader);
    }

    private TransformerRegistry createModifierRegistry() {
        TransformerRegistry transformerRegistry = new TransformerRegistry(defaultAgent,byteCodeInstrumentor);
        //transformerRegistry.addHttpURLConnectionTransformer();
        transformerRegistry.addDemoTransformer();
        return transformerRegistry;

    }

    @Override
    public byte[] transform(ClassLoader classLoader, String jvmClassName, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) throws IllegalClassFormatException {
        if (skipFilter.doFilter(classLoader, jvmClassName, classBeingRedefined, protectionDomain, classFileBuffer)) {
            return null;
        }
        AbstractClassFileTransformer findTransformer = this.transformerRegistry.findTransformer("*");
        if (findTransformer==null){
            System.out.println("findTransformer is null");
            return null;
        }
        final String javaClassName = JavaAssistUtils.jvmNameToJavaName(jvmClassName);

        try {
            final Thread thread = Thread.currentThread();
            final ClassLoader before = getContextClassLoader(thread);
            thread.setContextClassLoader(this.agentClassLoader);
            try {
                return findTransformer.modify(classLoader, javaClassName, protectionDomain, classFileBuffer);
            } finally {
                // The context class loader have to be recovered even if it was null.
                thread.setContextClassLoader(before);
            }
        }
        catch (Throwable e) {
            return null;
        }
    }

    private ClassLoader getContextClassLoader(Thread thread) {
        return thread.getContextClassLoader();
    }
}
