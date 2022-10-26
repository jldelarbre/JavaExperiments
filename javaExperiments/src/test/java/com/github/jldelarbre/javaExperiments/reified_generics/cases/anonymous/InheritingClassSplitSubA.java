package com.github.jldelarbre.javaExperiments.reified_generics.cases.anonymous;

import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.A;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.MyReifiedGeneric2Sub;

public abstract class InheritingClassSplitSubA<T, U extends A, V, W> extends InheritingClassSplitA<T, U> implements MyReifiedGeneric2Sub<V, W> {
    private final W param3;
    private final V param4;
    
    public InheritingClassSplitSubA(T param1, U param2, W param3, V param4) {
        super(param1, param2);
        this.param3 = param3;
        this.param4 = param4;
    }

    @Override
    public W getParam3() {
        return this.param3;
    }
    
    @Override
    public V getParam4() {
        return this.param4;
    }
}
