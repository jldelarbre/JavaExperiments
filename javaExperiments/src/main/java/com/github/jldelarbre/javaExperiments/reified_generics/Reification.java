package com.github.jldelarbre.javaExperiments.reified_generics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reification {

    //////////////////////////////
    // Reified generics package //
    //////////////////////////////
    
    public interface ReifiedGeneric<SELF extends ReifiedGeneric<SELF>> {
        
        default ReifiedGenericType<SELF> reifiedGenericType() {
            ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
            return new ReifiedGenericType<SELF>(){
                ParameterizedType parameterizedType() {
                    return parameterizedType;
                }
            };
        }
    }
    
    public static abstract class ReifiedGenericType<RG extends ReifiedGeneric<RG>> {
        ParameterizedType parameterizedType() {
            ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
            return (ParameterizedType) genericSuperclass.getActualTypeArguments()[0];
        }
        
        String getSimpleTypeName() {
            StringBuilder sb = new StringBuilder();
            extractSubTypeSimpleName(parameterizedType(), sb);
            return sb.toString();
        }

        private void extractSubTypeSimpleName(Type typeToProcess, StringBuilder sb) {
            if (typeToProcess instanceof ParameterizedType) {
                ParameterizedType castTypeToProcess = (ParameterizedType) typeToProcess;
                sb.append(Class.class.cast(castTypeToProcess.getRawType()).getSimpleName() + "<");
                boolean isFirst = true;
                for (Type type : castTypeToProcess.getActualTypeArguments()) {
                    if (!isFirst) {
                        sb.append(",");
                    }
                    extractSubTypeSimpleName(type, sb);
                    isFirst = false;
                }
                sb.append(">");
            } else {
                sb.append(Class.class.cast(typeToProcess).getSimpleName());
            }
        }
        
        // Enhancement: Only check if it is exact type not super type
        boolean isInstance(ReifiedGeneric<?> rg) {
            return rg.reifiedGenericType().parameterizedType().equals(parameterizedType());
        }
        
        RG cast(ReifiedGeneric<?> rg) {
            if (!isInstance(rg)) {
                String errMsg = rg.reifiedGenericType().getSimpleTypeName() + " is not castable to " + getSimpleTypeName();
                throw new ClassCastException(errMsg);
            }
            @SuppressWarnings("unchecked")
            RG castRg = (RG) rg;
            return castRg;
        }
    }
    
    //////////////////////////////////
    // Reified generics package END //
    //////////////////////////////////
    
    //////////////////////////////////////////////////////////////////////
    // Samples of reified generic types definitions and implementations //
    //////////////////////////////////////////////////////////////////////
    
    // Dummy class for tests
    interface IA1 {}
    interface IA2 extends IA1 {}
    static class A implements IA2 {}
    static class B extends A {}
    
    // Reified Generic Type Definition
    public interface MyReifiedGeneric<T, U extends A> extends ReifiedGeneric<MyReifiedGeneric<T, U>> {
        T getParam1();

        U getParam2();
    }
    
    // Reified Generic Type Implementation: type passed by constructor argument
    //
    // Impossible to implement ReifiedGeneric<MyReifiedGenericImpl1<T, U>>
    // otherwise ReifiedGeneric will be inherit with 2 different generic parameters:
    // MyReifiedGeneric<T, U> and MyReifiedGenericImpl1<T, U>
    //
    // Example: impossible to have:
    //     class SomeClass implements List<String>, List<Double> {...}
    static final class MyReifiedGenericImpl1<T, U extends A> implements MyReifiedGeneric<T, U>/*, ReifiedGeneric<MyReifiedGenericImpl1<T, U>>*/ {
        private final ReifiedGenericType<MyReifiedGeneric<T, U>> reifiedGenericType;
        
        private final T param1;
        private final U param2;
        
        MyReifiedGenericImpl1(ReifiedGenericType<MyReifiedGeneric<T, U>> reifiedGenericType, T param1, U param2) {
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
        public ReifiedGenericType<MyReifiedGeneric<T, U>> reifiedGenericType() {
            return reifiedGenericType;
        }
    }
    
    // Reified Generic Type Implementation:
    // final not allowed: use derived anonymous class to generate ParameterizedType info
    // Implementation could be hidden thanks to package visibility 
    static /* final */ class MyReifiedGenericImpl2<T, U extends A> implements MyReifiedGeneric<T, U> {
        private final T param1;
        private final U param2;
        
        MyReifiedGenericImpl2(T param1, U param2) {
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
    
    ////////////////////////////////////////////////////////////////////////////////
    // Define concrete parameterized types with factory methods to implement them //
    ////////////////////////////////////////////////////////////////////////////////
    
    public static ReifiedGenericType<MyReifiedGeneric<List<Integer>, A>> MyReifiedGenericᐸListᐸIntegerᐳιAᐳ =
        new ReifiedGenericType<MyReifiedGeneric<List<Integer>, A>>(){};
    
    // Impossible to define: ReifiedGenericType<MyReifiedGenericImpl1<List<Integer>, A>>
    // So impossible to inject the exact type for: MyReifiedGenericImpl1<List<Integer>, A>
    // 
    // For that: MyReifiedGenericImpl1<List<Integer>, A> should extend ReifiedGeneric<MyReifiedGenericImpl1<T, U>>
    // but not possible: see MyReifiedGenericImpl1 definition above
    //
//    static ReifiedGenericType<MyReifiedGenericImpl1<List<Integer>, A>> MyReifiedGenericImpl1ᐸListᐸIntegerᐳιAᐳ =
//        new ReifiedGenericType<MyReifiedGenericImpl1<List<Integer>, A>>(){};
    
    public static MyReifiedGeneric<List<Integer>, A> buildReifiedGeneric(List<Integer> p1, A p2) {
        // Build reified generic passing reified type as argument
        return new MyReifiedGenericImpl1<List<Integer>, A>(MyReifiedGenericᐸListᐸIntegerᐳιAᐳ, p1, p2);
    }
    
    public static ReifiedGenericType<MyReifiedGeneric<Map<String, List<Double>>, B>> MyReifiedGenericᐸMapᐸStringιListᐸDoubleᐳᐳιBᐳᐳ =
            new ReifiedGenericType<MyReifiedGeneric<Map<String, List<Double>>, B>>(){};
    
    public static MyReifiedGeneric<Map<String, List<Double>>, B> buildReifiedGeneric(Map<String, List<Double>> p1, B p2) {
        // Build reified generic infering reified type using anonymous class
    	return new MyReifiedGenericImpl2<Map<String, List<Double>>, B>(p1, p2){};
    }
    
    //////////////////
    // Test program //
    //////////////////
    
    public static void main(String[] args) {
        assertEquals("MyReifiedGeneric<List<Integer>,A>", MyReifiedGenericᐸListᐸIntegerᐳιAᐳ.getSimpleTypeName());
        assertEquals("MyReifiedGeneric<Map<String,List<Double>>,B>", MyReifiedGenericᐸMapᐸStringιListᐸDoubleᐳᐳιBᐳᐳ.getSimpleTypeName());
        
        MyReifiedGeneric<List<Integer>, A> reifiedGeneric1 = buildReifiedGeneric(new ArrayList<>(), (A) new B());
        MyReifiedGeneric<Map<String,List<Double>>,B> reifiedGeneric2 = buildReifiedGeneric(new HashMap<>(), new B());
        
        assertEquals("MyReifiedGeneric<List<Integer>,A>", reifiedGeneric1.reifiedGenericType().getSimpleTypeName());
        assertEquals("MyReifiedGenericImpl2<Map<String,List<Double>>,B>", reifiedGeneric2.reifiedGenericType().getSimpleTypeName());
        
        assertTrue(MyReifiedGenericᐸListᐸIntegerᐳιAᐳ.isInstance(reifiedGeneric1));
        assertFalse(MyReifiedGenericᐸListᐸIntegerᐳιAᐳ.isInstance(reifiedGeneric2));
//        assertTrue(MyReifiedGenericᐸMapᐸStringιListᐸDoubleᐳᐳιBᐳᐳ.isInstance(reifiedGeneric2));
        
        ReifiedGeneric<?> rg = reifiedGeneric1;
        MyReifiedGeneric<? extends List<Integer>, ? extends A> rg2 = reifiedGeneric1;
        MyReifiedGeneric<List<Integer>, A> castRG = MyReifiedGenericᐸListᐸIntegerᐳιAᐳ.cast(rg);
        MyReifiedGeneric<List<Integer>, A> castRG2 = MyReifiedGenericᐸListᐸIntegerᐳιAᐳ.cast(rg2);
        assertEquals(reifiedGeneric1, castRG);
        assertEquals(reifiedGeneric1, castRG2);
        
        assertThrows(ClassCastException.class, () -> MyReifiedGenericᐸListᐸIntegerᐳιAᐳ.cast(reifiedGeneric2));
        try {
            MyReifiedGenericᐸMapᐸStringιListᐸDoubleᐳᐳιBᐳᐳ.cast(reifiedGeneric1);
        } catch (ClassCastException e) {
            assertEquals("MyReifiedGeneric<List<Integer>,A> is not castable to MyReifiedGeneric<Map<String,List<Double>>,B>", e.getMessage());
        }
    }
}
