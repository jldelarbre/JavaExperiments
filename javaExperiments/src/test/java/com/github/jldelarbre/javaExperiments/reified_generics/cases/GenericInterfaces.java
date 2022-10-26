package com.github.jldelarbre.javaExperiments.reified_generics.cases;

import com.github.jldelarbre.javaExperiments.reified_generics.ReifiedGeneric;

public class GenericInterfaces {

    // Dummy class for tests
    public static class A {}
    public static class B extends A {}
    
    // Reified Generic Type Definitions
    public interface RGSuper<T> extends ReifiedGeneric {}
    
    public interface MyReifiedGeneric<T, U extends A> extends RGSuper<T> {
        T getParam1();

        U getParam2();
    }
    
    public interface MyReifiedGeneric2<T> extends ReifiedGeneric {
        T getParam3();
    }
    
    public interface MyReifiedGeneric2Sub<T, U> extends MyReifiedGeneric2<U> {
        T getParam4();
    }
}
