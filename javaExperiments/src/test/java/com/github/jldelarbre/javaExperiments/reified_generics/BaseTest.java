package com.github.jldelarbre.javaExperiments.reified_generics;

import static com.github.jldelarbre.javaExperiments.reified_generics.cases.Factories.MyReifiedGenericᐸListᐸIntegerᐳιAᐳ;
import static com.github.jldelarbre.javaExperiments.reified_generics.cases.Factories.MyReifiedGenericᐸMapᐸStringιListᐸDoubleᐳᐳιBᐳᐳ;
import static com.github.jldelarbre.javaExperiments.reified_generics.cases.Factories.SimpleClassBᐸListᐸIntegerᐳιAιStringιDoubleᐳ;
import static com.github.jldelarbre.javaExperiments.reified_generics.cases.Factories.buildReifiedGeneric;
import static com.github.jldelarbre.javaExperiments.reified_generics.cases.Factories.buildReifiedGeneric3;
import static com.github.jldelarbre.javaExperiments.reified_generics.cases.Factories.buildReifiedGeneric4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.A;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.B;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.GenericInterfaces.MyReifiedGeneric;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.anonymous.InheritingClassSplitSubA;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.anonymous.SimpleClassA;
import com.github.jldelarbre.javaExperiments.reified_generics.cases.hold_type_explicitly.InheritingClassSplitSubB;

public class BaseTest {

    @Test
    public void test() {
        InheritingClassSplitSubA<String, B, Integer, Double> buildReifiedGeneric3 = buildReifiedGeneric3("", new B(), 0.0, 1);
        ReifiedGenericType<?> reifiedGenericType = buildReifiedGeneric3.reifiedGenericType();
        
        InheritingClassSplitSubB<String, B, Integer, Double> buildReifiedGeneric4 =
            buildReifiedGeneric4(new ReifiedGenericType<InheritingClassSplitSubB<String, B, Integer, Double>>(){}, "", new B(), 0.0, 1);
        ReifiedGenericType<?> reifiedGenericType2 = buildReifiedGeneric4.reifiedGenericType();
        
        assertEquals("MyReifiedGeneric<List<Integer>,A>", MyReifiedGenericᐸListᐸIntegerᐳιAᐳ.getSimpleTypeName());
        assertEquals("MyReifiedGeneric<List<Integer>,A>", new ReifiedGenericType<MyReifiedGeneric<List<Integer>, A>>(){}.getSimpleTypeName());
        assertEquals(MyReifiedGenericᐸListᐸIntegerᐳιAᐳ.parameterizedType(),
                     new ReifiedGenericType<MyReifiedGeneric<List<Integer>, A>>(){}.parameterizedType());
        assertEquals("MyReifiedGeneric<Map<String,List<Double>>,B>", MyReifiedGenericᐸMapᐸStringιListᐸDoubleᐳᐳιBᐳᐳ.getSimpleTypeName());
        
        MyReifiedGeneric<List<Integer>, A> reifiedGeneric1 = buildReifiedGeneric(new ArrayList<>(), (A) new B());
        MyReifiedGeneric<Map<String,List<Double>>,B> reifiedGeneric2 = buildReifiedGeneric(new HashMap<>(), new B(), "", 0.0);
        
        assertEquals("SimpleClassB<List<Integer>,A,String,Double>", reifiedGeneric1.reifiedGenericType().getSimpleTypeName());
        assertEquals("SimpleClassA<Map<String,List<Double>>,B,Double,String>", reifiedGeneric2.reifiedGenericType().getSimpleTypeName());
        
        assertTrue(SimpleClassBᐸListᐸIntegerᐳιAιStringιDoubleᐳ.isInstance(reifiedGeneric1));
        assertFalse(MyReifiedGenericᐸListᐸIntegerᐳιAᐳ.isInstance(reifiedGeneric2));
        assertTrue(new ReifiedGenericType<SimpleClassA<Map<String, List<Double>>, B, Double, String>>(){}.isInstance(reifiedGeneric2));
        
        ReifiedGeneric rg = reifiedGeneric1;
        MyReifiedGeneric<? extends List<Integer>, ? extends A> rg2 = reifiedGeneric1;
//        MyReifiedGeneric<List<Integer>, A> castRG = MyReifiedGenericᐸListᐸIntegerᐳιAᐳ.cast(rg);
//        MyReifiedGeneric<List<Integer>, A> castRG2 = MyReifiedGenericᐸListᐸIntegerᐳιAᐳ.cast(rg2);
//        assertEquals(reifiedGeneric1, castRG);
//        assertEquals(reifiedGeneric1, castRG2);
        
        assertThrows(ClassCastException.class, () -> MyReifiedGenericᐸListᐸIntegerᐳιAᐳ.cast(reifiedGeneric2));
        try {
            MyReifiedGenericᐸMapᐸStringιListᐸDoubleᐳᐳιBᐳᐳ.cast(reifiedGeneric1);
        } catch (ClassCastException e) {
            assertEquals("SimpleClassB<List<Integer>,A,String,Double> is not castable to MyReifiedGeneric<Map<String,List<Double>>,B>", e.getMessage());
        }
    }

}
