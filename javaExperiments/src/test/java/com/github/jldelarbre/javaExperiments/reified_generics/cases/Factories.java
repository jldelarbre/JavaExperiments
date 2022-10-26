package com.github.jldelarbre.javaExperiments.reified_generics.cases;

import java.util.List;
import java.util.Map;

import com.github.jldelarbre.javaExperiments.reified_generics.ReifiedGenericType;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.A;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.B;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.MyReifiedGeneric;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.RGSuper;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.anonymous.InheritingClassSplitSubA;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.anonymous.SimpleClassA;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.hold_type_explicitly.InheritingClassB;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.hold_type_explicitly.InheritingClassSplitSubB;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.hold_type_explicitly.SimpleClassB;

public class Factories {

    public static final ReifiedGenericType<MyReifiedGeneric<List<Integer>, A>> MyReifiedGenericᐸListᐸIntegerᐳιAᐳ =
        new ReifiedGenericType<MyReifiedGeneric<List<Integer>, A>>(){};
        
    public static final ReifiedGenericType<SimpleClassB<List<Integer>, A, String, Double>> SimpleClassBᐸListᐸIntegerᐳιAιStringιDoubleᐳ =
        new ReifiedGenericType<SimpleClassB<List<Integer>, A, String, Double>>(){};
    
    public static MyReifiedGeneric<List<Integer>, A> buildReifiedGeneric(List<Integer> p1, A p2) {
        // Build reified generic passing reified type as argument
        return new SimpleClassB<List<Integer>, A, String, Double>(SimpleClassBᐸListᐸIntegerᐳιAιStringιDoubleᐳ, p1, p2, 0.0, "");
    }
    
    public static final ReifiedGenericType<MyReifiedGeneric<Map<String, List<Double>>, B>> MyReifiedGenericᐸMapᐸStringιListᐸDoubleᐳᐳιBᐳᐳ =
            new ReifiedGenericType<MyReifiedGeneric<Map<String, List<Double>>, B>>(){};
    
    public static SimpleClassA<Map<String, List<Double>>, B, Double, String> buildReifiedGeneric(Map<String, List<Double>> p1, B p2, String p3, Double p4) {
        // Build reified generic infering reified type using anonymous class
        return new SimpleClassA<Map<String, List<Double>>, B, Double, String>(p1, p2, p3, p4){};
    }
    
    public static MyReifiedGeneric<Map<String, List<Double>>, B> buildReifiedGeneric3Sub(Map<String, List<Double>> p1, B p2, String p3, Double p4) {
        // Build reified generic infering reified type using anonymous class
        return new InheritingClassSplitSubA<Map<String, List<Double>>, B, Double, String>(p1, p2, p3, p4){};
    }
    
    static final ReifiedGenericType<InheritingClassSplitSubB<Map<String, List<Double>>, B, Double, String>>
        InheritingClassSplitSubBᐸMapᐸStringιListᐸDoubleᐳᐳιBιDoubleιStringᐳᐳ  = 
        new ReifiedGenericType<InheritingClassSplitSubB<Map<String, List<Double>>, B, Double, String>>(){};
        
    public static MyReifiedGeneric<Map<String, List<Double>>, B> buildReifiedGeneric4Sub(Map<String, List<Double>> p1, B p2, String p3, Double p4) {
        return new InheritingClassSplitSubB<Map<String, List<Double>>, B, Double, String>(InheritingClassSplitSubBᐸMapᐸStringιListᐸDoubleᐳᐳιBιDoubleιStringᐳᐳ,
                                                                                          p1, p2, p3, p4);
    }
    
    public static <T, U extends A, V, W> InheritingClassSplitSubA<T, U, V, W> buildReifiedGeneric3(T p1, U p2, W p3, V p4) {
        // Do not work: T, U, V, W are not reified at runtime, types are erased
        return new InheritingClassSplitSubA<T, U, V, W>(p1, p2, p3, p4){};
    }
    
    // Factory method usable without defining a generic type with actual arguments set in advance
    public static <T, U extends A, V, W> InheritingClassSplitSubB<T, U, V, W>
        buildReifiedGeneric4(ReifiedGenericType<InheritingClassSplitSubB<T, U, V, W>> type, T p1, U p2, W p3, V p4) {
        
        return new InheritingClassSplitSubB<T, U, V, W>(type, p1, p2, p3, p4);
    }
    
    public static <T, U extends A> MyReifiedGeneric<T, U>
        buildReifiedGeneric(ReifiedGenericType<MyReifiedGeneric<T, U>> type, T p1, U p2) {
    
        return new InheritingClassB<T, U>(type, p1, p2);
    }
    
    public static <T> RGSuper<T>
        buildReifiedGenericBis(ReifiedGenericType<RGSuper<T>> type, T p1) {
        
        return new InheritingClassB<T, A>(new ReifiedGenericType<InheritingClassB<T, A>>(type){}, p1, new A());
    }
}
