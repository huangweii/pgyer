package com.pgyer.simple.pinpoint.trans.transformer;

import com.pgyer.simple.pinpoint.DefaultAgent;
import com.pgyer.simple.pinpoint.instrument.ByteCodeInstrumentor;
import com.pgyer.simple.pinpoint.transformer.demo.TestDemoTransformer;

import java.util.HashMap;
import java.util.Map;

public class TransformerRegistry {

    private final DefaultAgent defaultAgent;

    private final ByteCodeInstrumentor byteCodeInstrumentor;

    private final Map<String, AbstractClassFileTransformer> registry = new HashMap<String, AbstractClassFileTransformer>(512);

    public TransformerRegistry(DefaultAgent defaultAgent, ByteCodeInstrumentor byteCodeInstrumentor){
        this.defaultAgent=defaultAgent;
        this.byteCodeInstrumentor=byteCodeInstrumentor;
    }

    public void addDemoTransformer() {
        TestDemoTransformer testDemoTransformer=new TestDemoTransformer(defaultAgent,byteCodeInstrumentor);
        addModifier(testDemoTransformer);
    }

//    public void addHttpURLConnectionTransformer() {
//        HttpURLConnectionTransformer httpURLConnectionTransformer=new HttpURLConnectionTransformer(defaultAgent,byteCodeInstrumentor);
//        addModifier(httpURLConnectionTransformer);
//    }

    public void addModifier(AbstractClassFileTransformer transformer) {
        AbstractClassFileTransformer old = registry.put(transformer.getTargetClass(), transformer);
        if (old != null) {
            throw new IllegalStateException("Modifier already exist, old:" + old.getTargetClass());
        }
    }

    public AbstractClassFileTransformer findTransformer(String className) {
        return registry.get(className);
    }
}
