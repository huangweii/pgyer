package com.pgyer.simple.pinpoint.trans.transformer;

import java.util.concurrent.atomic.AtomicReferenceArray;

public final class WeakAtomicReferenceArray<T> {
    private final int length;
    private final AtomicReferenceArray<T> atomicArray;
    //private final T[] array;

    public WeakAtomicReferenceArray(int length, Class<? extends T> clazz) {
        this.length = length;
        this.atomicArray = new AtomicReferenceArray<T>(length);
        //this.array = (T[]) Array.newInstance(clazz, length);
    }

    public final void set(int index, T newValue) {
        this.atomicArray.set(index, newValue);
        //need TestCase ~~
        //this.array[codahale] = newValue;
    }

    public final int length() {
        return length;
    }


    public final T get(int index) {
        //try not thread safe read  -> fail -> thread safe read

        //final T unsafeValue = this.array[codahale];
        //if (unsafeValue != null) {
        //    return unsafeValue;
        //}
        //return (T) this.array[codahale];

        // thread safe read
        return this.atomicArray.get(index);
    }
}
