package com.github.jldelarbre.javaExperiments.reified_generics.cases.hold_type_explicitly;

import com.github.jldelarbre.javaExperiments.reified_generics.ReifiedGenericType;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.A;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.MyReifiedGeneric2Sub;

public final class InheritingClassSubB<T, U extends A, V, W> extends InheritingClassB<T, U> implements MyReifiedGeneric2Sub<V, W> {
    private final W param3;
    private final V param4;
    
    InheritingClassSubB(ReifiedGenericType<? super InheritingClassSubB<T, U, V, W>> reifiedGenericType, T param1, U param2, W param3, V param4) {
        super(() -> new ReifiedGenericType<InheritingClassSubB<T, U, V, W>>(reifiedGenericType.parameterizedType()){}, param1, param2); // select the right parameters
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
