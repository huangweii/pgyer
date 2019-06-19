package com.pgyer.simple.pinpoint.instrument.javaassist;

import com.pgyer.simple.pinpoint.context.TraceContext;
import com.pgyer.simple.pinpoint.instrument.InstrumentClass;
import com.pgyer.simple.pinpoint.instrument.InstrumentException;
import com.pgyer.simple.pinpoint.instrument.MethodFilter;
import com.pgyer.simple.pinpoint.instrument.MethodInfo;
import com.pgyer.simple.pinpoint.interceptor.*;
import com.pgyer.simple.pinpoint.trace.DefaultMethodDescriptor;
import com.pgyer.simple.pinpoint.tracevalue.MethodDescriptor;
import com.pgyer.simple.pinpoint.transformer.demo.SkipMethodFilter;
import com.pgyer.simple.pinpoint.utils.CodeBuilder;
import com.pgyer.simple.pinpoint.utils.JavaAssistUtils;
import javassist.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JavaAssistClass implements InstrumentClass {
    private static final int NOT_DEFINE_INTERCEPTOR_ID = -1;
    private static final int STATIC_INTERCEPTOR = 0;
    private static final int SIMPLE_INTERCEPTOR = 1;
    private final JavaAssistByteCodeInstrumentor instrumentor;
    private final CtClass ctClass;

    public JavaAssistClass(JavaAssistByteCodeInstrumentor instrumentor, CtClass ctClass) {
        this.instrumentor = instrumentor;
        this.ctClass = ctClass;
    }

    @Override
    public boolean isInterceptable() {
        return !ctClass.isInterface() && !ctClass.isAnnotation() && !ctClass.isModified();
    }

    @Override
    public List<MethodInfo> getDeclaredMethods() {
        return getDeclaredMethods(SkipMethodFilter.FILTER);
    }

    public List<MethodInfo> getDeclaredMethods(MethodFilter methodFilter) {
        if (methodFilter == null) {
            throw new NullPointerException("methodFilter must not be null");
        }
        final CtMethod[] declaredMethod = ctClass.getDeclaredMethods();
        final List<MethodInfo> candidateList = new ArrayList<MethodInfo>(declaredMethod.length);
        for (CtMethod ctMethod : declaredMethod) {
            final MethodInfo method = new JavassistMethodInfo(ctMethod);
            if (methodFilter.filter(method)) {
                continue;
            }

            candidateList.add(method);
        }

        return candidateList;
    }

    @Override
    public int addInterceptor(String methodName, String[] args, Interceptor interceptor) throws InstrumentException {
        if (methodName == null) {
            throw new NullPointerException("methodName must not be null");
        }
        if (interceptor == null) {
            throw new IllegalArgumentException("interceptor is null");
        }
        final CtBehavior behavior = getMethod(methodName, args);
        return addInterceptor0(behavior, methodName, interceptor, NOT_DEFINE_INTERCEPTOR_ID);
    }

    private int addInterceptor0(CtBehavior behavior, String methodName, Interceptor interceptor, int interceptorId) throws InstrumentException {
        try {
            if (interceptor != null) {
                if (interceptor instanceof SimpleAroundInterceptor) {
                    SimpleAroundInterceptor simpleAroundInterceptor = (SimpleAroundInterceptor) interceptor;
                    interceptorId = InterceptorRegistry.addSimpleInterceptor(simpleAroundInterceptor);
                } else {
                    throw new InstrumentException("unsupported Interceptor Type:" + interceptor);
                }
                injectInterceptor(behavior, interceptor);
            } else {
                interceptor = InterceptorRegistry.findInterceptor(interceptorId);
            }

            if (interceptor instanceof SimpleAroundInterceptor) {
                addSimpleAroundInterceptor(methodName, interceptorId, behavior);
            } else {
                throw new IllegalArgumentException("unsupported");
            }
            return interceptorId;
        } catch (NotFoundException e) {
            throw new InstrumentException(getInterceptorName(interceptor) + " add fail. Cause:" + e.getMessage(), e);
        } catch (CannotCompileException e) {
            throw new InstrumentException(getInterceptorName(interceptor) + "add fail. Cause:" + e.getMessage(), e);
        }
    }

    private void addSimpleAroundInterceptor(String methodName, int interceptorId, CtBehavior behavior) throws NotFoundException, CannotCompileException {
        addSimpleBeforeInterceptor(methodName, interceptorId, behavior);
        addSimpleAfterInterceptor(methodName, interceptorId, behavior);
    }

    private void addSimpleBeforeInterceptor(String methodName, int interceptorId, CtBehavior behavior) throws NotFoundException, CannotCompileException {
        addBeforeInterceptor(methodName, interceptorId, behavior, SIMPLE_INTERCEPTOR);
    }

    private void addSimpleAfterInterceptor(String methodName, int interceptorId, CtBehavior behavior) throws NotFoundException, CannotCompileException {
        addAfterInterceptor(methodName, interceptorId, behavior, SIMPLE_INTERCEPTOR);
    }

    private void addBeforeInterceptor(String methodName, int id, CtBehavior behavior, int interceptorType) throws CannotCompileException, NotFoundException {
        final String target = getTargetIdentifier(behavior);

        //返回方法签名（参数类型和返回类型）。
        final String[] parameterType = JavaAssistUtils.parseParameterSignature(behavior.getSignature());

        // If possible, use static data to reduce interceptor overhead.
        String parameterDescription = null;
        final String parameterIdentifier = getParameterIdentifier(parameterType);

        CodeBuilder code = new CodeBuilder();
        code.begin();
        code.format("%1$s interceptor = com.pgyer.simple.pinpoint.interceptor.InterceptorRegistry.getSimpleInterceptor(%2$d);", SimpleAroundInterceptor.class.getName(), id);
        code.format("interceptor.before(%1$s, %2$s);", target, parameterIdentifier);
        code.end();
        String buildBefore = code.toString();

        if (behavior instanceof CtConstructor) {
            ((CtConstructor) behavior).insertBeforeBody(buildBefore);
        } else {
            behavior.insertBefore(buildBefore);
        }
    }

    private String getTargetIdentifier(CtBehavior behavior) {
        boolean staticMethod = JavaAssistUtils.isStaticBehavior(behavior);
        if (staticMethod) {
            return "null";
        } else {
            return "this";
        }
    }

    private String getParameterIdentifier(String[] parameterTypes) {
        if (parameterTypes.length == 0) {
            return "null";
        }
        return "$args";
    }

    private void addAfterInterceptor(String methodName, int id, CtBehavior behavior, int interceptorType) throws NotFoundException, CannotCompileException {
        final String returnType = getReturnType(behavior);
        final String target = getTargetIdentifier(behavior);

        final String[] parameterType = JavaAssistUtils.parseParameterSignature(behavior.getSignature());
        String parameterTypeString = null;
        final String parameterIdentifier = getParameterIdentifier(parameterType);

        final CodeBuilder after = new CodeBuilder();
        after.begin();

        after.format("  %1$s interceptor = com.pgyer.simple.pinpoint.interceptor.InterceptorRegistry.getSimpleInterceptor(%2$d);", SimpleAroundInterceptor.class.getName(), id);
        after.format("  interceptor.after(%1$s, %2$s, %3$s, null);", target, parameterIdentifier, returnType);
        after.end();
        final String buildAfter = after.toString();

        behavior.insertAfter(buildAfter);
    }

    public String getReturnType(CtBehavior behavior) {
        if (behavior instanceof CtMethod) {
            final String signature = behavior.getSignature();
            if (isVoid(signature)) {
                return "null";
            }
        }
        return "($w)$_";
    }

    public boolean isVoid(String signature) {
        return signature.endsWith("V");
    }

    private void injectInterceptor(CtBehavior behavior, Interceptor interceptor) {
        if (interceptor instanceof TraceContextSupport) {
            final TraceContext traceContext = instrumentor.getAgent().getTraceContext();
            ((TraceContextSupport) interceptor).setTraceContext(traceContext);
        }
        if (interceptor instanceof ByteCodeMethodDescriptorSupport) {
            final MethodDescriptor methodDescriptor = createMethodDescriptor(behavior);
            ((ByteCodeMethodDescriptorSupport) interceptor).setMethodDescriptor(methodDescriptor);
        }
    }

    private MethodDescriptor createMethodDescriptor(CtBehavior behavior) {
        DefaultMethodDescriptor methodDescriptor = new DefaultMethodDescriptor();

        String methodName = behavior.getName();
        methodDescriptor.setMethodName(methodName);

        methodDescriptor.setClassName(ctClass.getName());

        return methodDescriptor;
    }

    private CtMethod getMethod(String methodName, String[] args) throws InstrumentException {
        for (CtMethod method : ctClass.getDeclaredMethods()) {
            if (!method.getName().equals(methodName)) {
                continue;
            }
            return method;
        }
        throw new InstrumentException(methodName + Arrays.toString(args) + " is not found in " + this.getName());
    }

    private String getInterceptorName(Interceptor interceptor) {
        if (interceptor == null) {
            return "null";
        }
        return interceptor.getClass().getSimpleName();
    }

    @Override
    public byte[] toBytecode() throws InstrumentException {
        try {
            byte[] bytes = ctClass.toBytecode();
            ctClass.detach();
            return bytes;
        } catch (IOException e) {
            System.out.println("IoException class:{} Caused:{}" + ctClass.getName() + e.getMessage() + e);
        } catch (CannotCompileException e) {
            System.out.println("CannotCompileException class:{} Caused:{}" + ctClass.getName() + e.getMessage());
        }
        return null;
    }

    @Override
    public String getName() {
        return this.ctClass.getName();
    }
}
