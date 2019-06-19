package com.pgyer.simple.pinpoint;

import com.pgyer.simple.pinpoint.context.DefaultTraceContext;
import com.pgyer.simple.pinpoint.context.TraceContext;
import com.pgyer.simple.pinpoint.interfac.Agent;
import com.pgyer.simple.pinpoint.instrument.ByteCodeInstrumentor;
import com.pgyer.simple.pinpoint.instrument.javaassist.JavaAssistByteCodeInstrumentor;
import com.pgyer.simple.pinpoint.trans.transformer.TransformerDispatcher;

import java.lang.instrument.Instrumentation;

public class DefaultAgent implements Agent {

    private final TraceContext traceContext;

    private final ByteCodeInstrumentor defaultClassPool;

    public DefaultAgent(Instrumentation instrumentation ){
        this.traceContext = new DefaultTraceContext();

        this.defaultClassPool=new JavaAssistByteCodeInstrumentor(this);

//        ClassFileRetransformer retransformer=new ClassFileRetransformer(instrumentation);
//        instrumentation.addTransformer(retransformer, true);
        TransformerDispatcher transformerDispatcher = new TransformerDispatcher(this,defaultClassPool);
        instrumentation.addTransformer(transformerDispatcher);

        //for (Class classLoaded : instrumentation.getAllLoadedClasses()) {
            //System.out.println("*"+classLoaded.getName());
//            if (classLoaded.getName().startsWith("com.pgyer.")||classLoaded.getName().startsWith("javassist.")){
//                continue;
//            }
//            instrumentation.retransformClasses(classLoaded);
//            if (classLoaded.getName().equals("java.io.PrintStream")){
//                System.out.println(classLoaded.getName());
//                instrumentation.retransformClasses(classLoaded);
//            }
//            if (classLoaded.getName().equals("java.lang.String")){
//                System.out.println(classLoaded.getName());
//                instrumentation.retransformClasses(classLoaded);
//            }
//            if (classLoaded.getName().startsWith("com.trace.mano")) {
//                //System.out.println(classLoaded.getName());
//                instrumentation.retransformClasses(classLoaded);
//            }
       //}

        //System.out.println("add HttpURLConnectionTransformer to vm");
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    public TraceContext getTraceContext() {
        return traceContext;
    }

}
