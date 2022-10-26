package com.github.jldelarbre.javaExperiments.reified_generics;

import java.lang.reflect.ParameterizedType;

public interface ReifiedGeneric {
    
    default <SELF extends ReifiedGeneric> ReifiedGenericType<? extends ReifiedGeneric> reifiedGenericType() {
        ReifiedGenericType.checkUseAnonymousClass(getClass());
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return new ReifiedGenericType<SELF>(parameterizedType){};
    }
}
