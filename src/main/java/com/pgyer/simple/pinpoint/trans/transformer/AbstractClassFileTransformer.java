package com.pgyer.simple.pinpoint.trans.transformer;

import com.pgyer.simple.pinpoint.interfac.Agent;
import com.pgyer.simple.pinpoint.instrument.ByteCodeInstrumentor;

public abstract class AbstractClassFileTransformer implements Transformer{
    protected final ByteCodeInstrumentor byteCodeInstrumentor;
    protected final Agent agent;

    public AbstractClassFileTransformer(Agent agent,ByteCodeInstrumentor byteCodeInstrumentor) {
        if (byteCodeInstrumentor == null) {
            throw new NullPointerException("byteCodeInstrumentor must not be null");
        }
        if (agent == null) {
            throw new NullPointerException("agent must not be null");
        }
        this.byteCodeInstrumentor = byteCodeInstrumentor;
        this.agent = agent;
    }

    public Agent getAgent() {
        return agent;
    }

    public abstract String getTargetClass();

}
