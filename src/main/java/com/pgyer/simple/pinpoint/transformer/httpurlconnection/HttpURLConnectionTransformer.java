//package com.pgyer.simple.pinpoint.transformer.connect.httpurlconnection;
//
//import com.pgyer.simple.pinpoint.context.TraceContext;
//import com.pgyer.simple.pinpoint.pool.DefaultClassPool;
//import com.pgyer.simple.pinpoint.trace.DefaultMethodDescriptor;
//import com.pgyer.simple.pinpoint.trans.transformer.AbstractClassFileTransformer;
//import com.pgyer.simple.pinpoint.interceptor.InterceptorRegistry;
//import com.pgyer.simple.pinpoint.interceptor.SimpleAroundInterceptor;
//import ConnectMethodInterceptor;
//import com.pgyer.simple.pinpoint.utils.CodeBuilder;
//import com.pgyer.simple.pinpoint.utils.JavaAssistUtils;
//import javassist.*;
//
//import java.security.ProtectionDomain;
//
//public class HttpURLConnectionTransformer implements AbstractClassFileTransformer {
//
//    private TraceContext traceContext;
//
//    public HttpURLConnectionTransformer(TraceContext traceContext) {
//        this.traceContext = traceContext;
//    }
//
//
//    @Override
//    public byte[] modify(ClassLoader loader, String className, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
//        byte[] transformed = null;
//
//        DefaultClassPool pool =new DefaultClassPool();
//
//        CtClass ctClass = null;
//        try {
//            ctClass = pool.get(loader,className.replace('/', '.'));
//            String ctClassName=ctClass.getName();
//            System.out.println("ctClass name: "+ctClassName);
//            if (ctClassName.equals("java.lang.InterruptedException")) {
//                System.out.println("1"+ctClass.getDeclaredMethods()[0].getName());
//            }
//            if(ctClassNameCheck(ctClassName)){
//                //System.out.println(ctClass.getPackageName());
//                return classfileBuffer;
//            }
//
//            for (CtMethod method : ctClass.getDeclaredMethods()) {
//                if (ctClassName.equals("java.lang.InterruptedException")) {
//                    System.out.println("1"+method.getName());
//                }
//                if (Modifier.isNative(method.getModifiers())||Modifier.isAbstract(method.getModifiers())){
//                    continue;
//                }
//                ConnectMethodInterceptor connectMethodInterceptor = new ConnectMethodInterceptor();
//                int interceptorId = InterceptorRegistry.addSimpleInterceptor(connectMethodInterceptor);
//                injectInterceptor(ctClass, method, connectMethodInterceptor);
//
//                method.insertBefore(insertBeforeInterceptor(method, interceptorId));
//                method.insertAfter(insertAfterInterceptor(method, interceptorId));
//            }
//            transformed = ctClass.toBytecode();
//        } catch (CannotCompileException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (ctClass != null) {
//                ctClass.detach();
//            }
//        }
//        return transformed;
//    }
//
//    private boolean ctClassNameCheck(String ctClassName) {
//        return ctClassName.startsWith("com.pgyer")||ctClassName.startsWith("sun.launcher")||ctClassName.equals("java.lang.StackTraceElement$HashedModules")
//                ||ctClassName.equals("java.lang.Throwable$PrintStreamOrWriter")||ctClassName.equals("java.util.Random")
//                ||ctClassName.equals("java.lang.Throwable$WrappedPrintStream");
//    }
//
//    private void injectInterceptor(CtClass ctClass, CtMethod method, SimpleAroundInterceptor interceptor) {
//        //traceContext
//        interceptor.setTraceContext(traceContext);
//
//        //MethodDescriptor
//        DefaultMethodDescriptor methodDescriptor = new DefaultMethodDescriptor();
//        methodDescriptor.setClassName(ctClass.getName());
//        methodDescriptor.setMethodName(method.getName());
//        interceptor.setMethodDescriptor(methodDescriptor);
//    }
//
//    private String insertBeforeInterceptor(CtBehavior m, int id) {
//        final String target = getTargetIdentifier(m);
//
//        final String[] parameterType = JavaAssistUtils.parseParameterSignature(m.getSignature());
//        final String parameterIdentifier = getParameterIdentifier(parameterType);
//
//        CodeBuilder before = new CodeBuilder();
//        before.begin();
//        before.format("%1$s interceptor = com.pgyer.simple.pinpoint.transformer.InterceptorRegistry.getSimpleInterceptor(%2$d);", SimpleAroundInterceptor.class.getName(), id);
//        before.format("interceptor.before(%1$s,%2$s);", target, parameterIdentifier);
//        before.end();
//        return before.toString();
//    }
//
//    private String getTargetIdentifier(CtBehavior m) {
//        boolean staticMethod = JavaAssistUtils.isStaticBehavior(m);
//        if (staticMethod) {
//            return "null";
//        } else {
//            return "this";
//        }
//    }
//
//    private String getParameterIdentifier(String[] parameterTypes) {
//        if (parameterTypes.length == 0) {
//            return "null";
//        }
//        return "$args";
//    }
//
//    private String insertAfterInterceptor(CtBehavior m, int id) {
//        final String target = getTargetIdentifier(m);
//        final String returnType = getReturnType(m);
//
//        final String[] parameterType = JavaAssistUtils.parseParameterSignature(m.getSignature());
//        final String parameterIdentifier = getParameterIdentifier(parameterType);
//
//        CodeBuilder after = new CodeBuilder();
//        after.begin();
//        after.format("%1$s interceptor = com.pgyer.simple.pinpoint.transformer.InterceptorRegistry.getSimpleInterceptor(%2$d);", SimpleAroundInterceptor.class.getName(), id);
//        after.format("interceptor.after(%1$s, %2$s, %3$s, null);", target, parameterIdentifier, returnType);
//        after.end();
//        return after.toString();
//    }
//
//    private String getReturnType(CtBehavior behavior) {
//        if (behavior instanceof CtMethod) {
//            final String signature = behavior.getSignature();
//            if (isVoid(signature)) {
//                return "null";
//            }
//        }
//        return "($w)$_";
//    }
//
//    private boolean isVoid(String signature) {
//        return signature.endsWith("V");
//    }
//
//    public String getTargetClass() {
//        return "simple";
//    }
//}
