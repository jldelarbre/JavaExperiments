package com.github.jldelarbre.javaExperiments.reified_generics.cases.anonymous;

import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.A;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.MyReifiedGeneric;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.MyReifiedGeneric2Sub;

//Reified Generic Type Implementation:
// final not allowed: use derived anonymous class to generate ParameterizedType info
// Implementation could be hidden thanks to package visibility
public /* final */ class SimpleClassA<T, U extends A, V, W> implements MyReifiedGeneric<T, U>, MyReifiedGeneric2Sub<V, W> {
    private final T param1;
    private final U param2;
    private final W param3;
    private final V param4;
    
    public SimpleClassA(T param1, U param2, W param3, V param4) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
    }

    @Override
    public T getParam1() {
        return this.param1;
    }

    @Override
    public U getParam2() {
        return this.param2;
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
