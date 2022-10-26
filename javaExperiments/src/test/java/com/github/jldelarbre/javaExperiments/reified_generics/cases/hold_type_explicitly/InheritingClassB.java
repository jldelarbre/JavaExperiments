package com.github.jldelarbre.javaExperiments.reified_generics.cases.hold_type_explicitly;

import java.util.function.Supplier;

import com.github.jldelarbre.javaExperiments.reified_generics.ReifiedGeneric;
import com.github.jldelarbre.javaExperiments.reified_generics.ReifiedGenericType;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.A;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.MyReifiedGeneric;


public class InheritingClassB<T, U extends A> implements MyReifiedGeneric<T, U> {
    private final ReifiedGenericType<? extends InheritingClassB<T, U>> reifiedGenericType;
    
    private final T param1;
    private final U param2;
    
    public InheritingClassB(ReifiedGenericType<? super InheritingClassB<T, U>> reifiedGenericType, T param1, U param2) {
        this(() -> new ReifiedGenericType<InheritingClassB<T, U>>(reifiedGenericType.parameterizedType()){}, param1, param2); // Wrong pass parameters types only
    }
    
    InheritingClassB(Supplier<ReifiedGenericType<? extends InheritingClassB<T, U>>> reifiedGenericType, T param1, U param2) {
        this.reifiedGenericType = reifiedGenericType.get();
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
