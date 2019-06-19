package com.pgyer.simple.pinpoint.trace;

import java.util.Random;

public class SpanId {
    public static final long NULL = -1;

//    private static final Random seed = new Random();

    public static long newSpanId() {
        final Random random = getRandom();

        return createSpanId(random);
    }

    private static Random getRandom() {
        return new Random();
    }

    private static long createSpanId(Random seed) {
        long id = seed.nextLong();
        while (id == NULL) {
            id = seed.nextLong();
        }
        return id;
    }

    public static long nextSpanID(long spanId, long parentSpanId) {
        final Random seed = getRandom();

        long newId = createSpanId(seed);
        while (newId == spanId || newId == parentSpanId) {
            newId = createSpanId(seed);
        }
        return newId;
    }
}
