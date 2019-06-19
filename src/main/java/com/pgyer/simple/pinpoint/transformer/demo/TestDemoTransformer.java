package com.pgyer.simple.pinpoint.transformer.demo;

import com.pgyer.simple.pinpoint.DefaultAgent;
import com.pgyer.simple.pinpoint.instrument.ByteCodeInstrumentor;
import com.pgyer.simple.pinpoint.instrument.InstrumentClass;
import com.pgyer.simple.pinpoint.instrument.MethodInfo;
import com.pgyer.simple.pinpoint.interceptor.Interceptor;
import com.pgyer.simple.pinpoint.trans.transformer.AbstractClassFileTransformer;
import com.pgyer.simple.pinpoint.interceptor.SimpleAroundInterceptor;
import com.pgyer.simple.pinpoint.transformer.demo.interceptor.RunAInterceptor;
import com.pgyer.simple.pinpoint.utils.CodeBuilder;
import com.pgyer.simple.pinpoint.utils.JavaAssistUtils;
import javassist.*;

import java.security.ProtectionDomain;
import java.util.List;

public class TestDemoTransformer extends AbstractClassFileTransformer {

    public TestDemoTransformer(DefaultAgent defaultAgent, ByteCodeInstrumentor byteCodeInstrumentor) {
        super(defaultAgent,byteCodeInstrumentor);
    }

    public byte[] modify(ClassLoader loader, String className, ProtectionDomain protectionDomain, byte[] classFileBuffer) {
        try {
            InstrumentClass clazz = byteCodeInstrumentor.getClass(loader, className, classFileBuffer);

            if (!clazz.isInterceptable()) {
                return null;
            }

            List<MethodInfo> methodList = clazz.getDeclaredMethods();
            for (MethodInfo method : methodList) {
                final Interceptor interceptor = new RunAInterceptor();
                clazz.addInterceptor(method.getName(), method.getParameterTypes(), interceptor);
            }

            return clazz.toBytecode();
        } catch (Exception e) {
            System.out.println("TestDemoTransformer modify exception： "+e);
            return null;
        }

    }

    public String getTargetClass() {
        return "*";
    }
}
