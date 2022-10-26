package com.github.jldelarbre.javaExperiments.reified_generics.cases.hold_type_explicitly;

import com.github.jldelarbre.javaExperiments.reified_generics.ReifiedGeneric;
import com.github.jldelarbre.javaExperiments.reified_generics.ReifiedGenericType;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.A;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.MyReifiedGeneric;

public class InheritingClassSplitB<T, U extends A> implements MyReifiedGeneric<T, U> {
    private final ReifiedGenericType<? extends InheritingClassSplitB<T, U>> reifiedGenericType;
    private final T param1;
    private final U param2;
    
    InheritingClassSplitB(ReifiedGenericType<? extends InheritingClassSplitB<T, U>> reifiedGenericType, T param1, U param2) {
        this.reifiedGenericType = reifiedGenericType;
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

    @Override
    public <SELF extends ReifiedGeneric> ReifiedGenericType<? extends ReifiedGeneric> reifiedGenericType() {
        return reifiedGenericType;
    }
}
