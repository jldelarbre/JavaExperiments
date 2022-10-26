package com.github.jldelarbre.javaExperiments.reified_generics.cases.anonymous;

import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.A;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.MyReifiedGeneric;

public class InheritingClassSplitA<T, U extends A> implements MyReifiedGeneric<T, U> {
    private final T param1;
    private final U param2;
    
    InheritingClassSplitA(T param1, U param2) {
        this.param1 = param1;
        this.param2 = param2;
    }

    @Override
    public T getParam1() {
        return this.param1;
    }

    @Override
    public U getParam2() {
        return this.param2;
    }
}
