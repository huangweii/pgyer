package com.pgyer.simple.pinpoint.instrument.javaassist;

import com.pgyer.simple.pinpoint.instrument.MethodInfo;
import com.pgyer.simple.pinpoint.utils.JavaAssistUtils;
import javassist.CtBehavior;

public class JavassistMethodInfo implements MethodInfo {

    private final CtBehavior behavior;

    public JavassistMethodInfo(CtBehavior behavior) {
        this.behavior = behavior;
    }
    @Override
    public String getName() {
        return behavior.getName();
    }

    @Override
    public String[] getParameterTypes() {
        return JavaAssistUtils.parseParameterSignature(behavior.getSignature());
    }

}
