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
import java.util.function.Supplier;

public class Reification {

    //////////////////////////////
    // Reified generics package //
    //////////////////////////////
    
    public interface ReifiedGeneric {

        default <SELF extends ReifiedGeneric> ReifiedGenericType<? extends ReifiedGeneric> reifiedGenericType() {
            ReifiedGenericType.checkUseAnonymousClass(getClass());
            ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
            return new ReifiedGenericType<SELF>(parameterizedType){};
        }
    }
    
    public static abstract class ReifiedGenericType<RG extends ReifiedGeneric> {
        
        // implements hashcode and equals ?
        
        private final ParameterizedType parameterizedType;
        
        public ReifiedGenericType() {
            checkUseAnonymousClass(getClass());
            ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
            parameterizedType = (ParameterizedType) genericSuperclass.getActualTypeArguments()[0];
        }
        
        public ReifiedGenericType(ParameterizedType parameterizedType) {
            this.parameterizedType = parameterizedType;
        }
        
        static void checkUseAnonymousClass(Class<?> clazz) {
            if (!clazz.isAnonymousClass()) {
                throw new RuntimeException("Default implementation of reifiedGenericType() only works with anonymous classes, " +
                                           "otherwise reimplement the method.");
            }
        }
        
        public ParameterizedType parameterizedType() {
            return parameterizedType;
        }
        
        public String getSimpleTypeName() {
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
        public boolean isInstance(ReifiedGeneric rg) {
            return rg.reifiedGenericType().parameterizedType().equals(parameterizedType());
        }
        
        public RG cast(ReifiedGeneric rg) {
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
    static class A {}
    static class B extends A {}
    
    // Reified Generic Type Definition
    public interface MyReifiedGeneric<T, U extends A> extends ReifiedGeneric {
        T getParam1();

        U getParam2();
    }
    
    public interface MyReifiedGeneric2<T> extends ReifiedGeneric {
        T getParam3();
    }
    
    public interface MyReifiedGeneric2Sub<T, U> extends MyReifiedGeneric2<U> {
        T getParam4();
    }
    
    // Reified Generic Type Implementation: type passed by constructor argument
    static final class MyReifiedGenericImpl1<T, U extends A, V, W> implements MyReifiedGeneric<T, U>, MyReifiedGeneric2Sub<V, W> {
        private final ReifiedGenericType<MyReifiedGenericImpl1<T, U, V, W>> reifiedGenericType;
        
        private final T param1;
        private final U param2;
        private final W param3;
        private final V param4;
        
        MyReifiedGenericImpl1(ReifiedGenericType<MyReifiedGenericImpl1<T, U, V, W>> reifiedGenericType, T param1, U param2, W param3, V param4) {
            this.reifiedGenericType = reifiedGenericType;
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

        @Override
        public <SELF extends ReifiedGeneric> ReifiedGenericType<? extends ReifiedGeneric> reifiedGenericType() {
            return reifiedGenericType;
        }
    }
    
    // Reified Generic Type Implementation:
    // final not allowed: use derived anonymous class to generate ParameterizedType info
    // Implementation could be hidden thanks to package visibility 
    static /* final */ class MyReifiedGenericImpl2<T, U extends A, V, W> implements MyReifiedGeneric<T, U>, MyReifiedGeneric2Sub<V, W> {
        private final T param1;
        private final U param2;
        private final W param3;
        private final V param4;
        
        MyReifiedGenericImpl2(T param1, U param2, W param3, V param4) {
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
    
    static class MyReifiedGenericImpl3<T, U extends A> implements MyReifiedGeneric<T, U> {
        private final T param1;
        private final U param2;
        
        MyReifiedGenericImpl3(T param1, U param2) {
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
    
    static abstract class MyReifiedGenericImpl3Sub<T, U extends A, V, W> extends MyReifiedGenericImpl3<T, U> implements MyReifiedGeneric2Sub<V, W> {
        private final W param3;
        private final V param4;
        
        MyReifiedGenericImpl3Sub(T param1, U param2, W param3, V param4) {
            super(param1, param2);
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
    
    static class MyReifiedGenericImpl4<T, U extends A> implements MyReifiedGeneric<T, U> {
        private final ReifiedGenericType<? extends MyReifiedGenericImpl4<T, U>> reifiedGenericType;
        private final T param1;
        private final U param2;
        
        MyReifiedGenericImpl4(ReifiedGenericType<? extends MyReifiedGenericImpl4<T, U>> reifiedGenericType, T param1, U param2) {
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
    
    static class MyReifiedGenericImpl4Sub<T, U extends A, V, W> extends MyReifiedGenericImpl4<T, U> implements MyReifiedGeneric2Sub<V, W> {
        private final W param3;
        private final V param4;
        
        MyReifiedGenericImpl4Sub(ReifiedGenericType<? extends MyReifiedGenericImpl4Sub<T, U, V, W>> reifiedGenericType, T param1, U param2, W param3, V param4) {
            super(reifiedGenericType, param1, param2);
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
    
    ////////////////////////////////////////////////////////////////////////////////
    // Define concrete parameterized types with factory methods to implement them //
    ////////////////////////////////////////////////////////////////////////////////
    
    public static final ReifiedGenericType<MyReifiedGeneric<List<Integer>, A>> MyReifiedGenericᐸListᐸIntegerᐳιAᐳ =
        new ReifiedGenericType<MyReifiedGeneric<List<Integer>, A>>(){};
        
    static final ReifiedGenericType<MyReifiedGenericImpl1<List<Integer>, A, String, Double>> MyReifiedGenericImpl1ᐸListᐸIntegerᐳιAιStringιDoubleᐳ =
        new ReifiedGenericType<MyReifiedGenericImpl1<List<Integer>, A, String, Double>>(){};
    
    public static MyReifiedGeneric<List<Integer>, A> buildReifiedGeneric(List<Integer> p1, A p2) {
        // Build reified generic passing reified type as argument
        return new MyReifiedGenericImpl1<List<Integer>, A, String, Double>(MyReifiedGenericImpl1ᐸListᐸIntegerᐳιAιStringιDoubleᐳ, p1, p2, 0.0, "");
    }
    
    public static final ReifiedGenericType<MyReifiedGeneric<Map<String, List<Double>>, B>> MyReifiedGenericᐸMapᐸStringιListᐸDoubleᐳᐳιBᐳᐳ =
            new ReifiedGenericType<MyReifiedGeneric<Map<String, List<Double>>, B>>(){};
    
    public static MyReifiedGeneric<Map<String, List<Double>>, B> buildReifiedGeneric(Map<String, List<Double>> p1, B p2, String p3, Double p4) {
        // Build reified generic infering reified type using anonymous class
    	return new MyReifiedGenericImpl2<Map<String, List<Double>>, B, Double, String>(p1, p2, p3, p4){};
    }
    
    public static MyReifiedGeneric<Map<String, List<Double>>, B> buildReifiedGeneric3Sub(Map<String, List<Double>> p1, B p2, String p3, Double p4) {
        // Build reified generic infering reified type using anonymous class
        return new MyReifiedGenericImpl3Sub<Map<String, List<Double>>, B, Double, String>(p1, p2, p3, p4){};
    }
    
    static final ReifiedGenericType<MyReifiedGenericImpl4Sub<Map<String, List<Double>>, B, Double, String>>
        MyReifiedGenericImpl4SubᐸMapᐸStringιListᐸDoubleᐳᐳιBιDoubleιStringᐳᐳ  = 
        new ReifiedGenericType<MyReifiedGenericImpl4Sub<Map<String, List<Double>>, B, Double, String>>(){};
        
    public static MyReifiedGeneric<Map<String, List<Double>>, B> buildReifiedGeneric4Sub(Map<String, List<Double>> p1, B p2, String p3, Double p4) {
        return new MyReifiedGenericImpl4Sub<Map<String, List<Double>>, B, Double, String>(MyReifiedGenericImpl4SubᐸMapᐸStringιListᐸDoubleᐳᐳιBιDoubleιStringᐳᐳ,
                                                                                          p1, p2, p3, p4);
    }
    
    public static <T, U extends A, V, W> MyReifiedGenericImpl3Sub<T, U, V, W> buildReifiedGeneric3(T p1, U p2, W p3, V p4) {
        // Do not work: T, U, V, W are not reified at runtime, types are erased
        return new MyReifiedGenericImpl3Sub<T, U, V, W>(p1, p2, p3, p4){};
    }
    
    // Factory method usable without defining a generic type with actual arguments set in advance
    public static <T, U extends A, V, W> MyReifiedGenericImpl4Sub<T, U, V, W>
        buildReifiedGeneric4(ReifiedGenericType<MyReifiedGenericImpl4Sub<T, U, V, W>> type, T p1, U p2, W p3, V p4) {
        
        return new MyReifiedGenericImpl4Sub<T, U, V, W>(type, p1, p2, p3, p4);
    }
    
    //////////////////
    // Test program //
    //////////////////
    
    public static void main(String[] args) {
        
        // ReifiedGenericType with actual argument as T, U, V, W
        MyReifiedGenericImpl3Sub<String, B, Integer, Double> buildReifiedGeneric3 = buildReifiedGeneric3("", new B(), 0.0, 1);
        ReifiedGenericType<?> reifiedGenericType = buildReifiedGeneric3.reifiedGenericType();
        
        // ReifiedGenericType with actual argument as String, B, Integer, Double
        MyReifiedGenericImpl4Sub<String, B, Integer, Double> buildReifiedGeneric4 =
            buildReifiedGeneric4(new ReifiedGenericType<MyReifiedGenericImpl4Sub<String, B, Integer, Double>>(){}, "", new B(), 0.0, 1);
        ReifiedGenericType<?> reifiedGenericType2 = buildReifiedGeneric4.reifiedGenericType();
        
        assertEquals("MyReifiedGeneric<List<Integer>,A>", MyReifiedGenericᐸListᐸIntegerᐳιAᐳ.getSimpleTypeName());
        assertEquals("MyReifiedGeneric<List<Integer>,A>", new ReifiedGenericType<MyReifiedGeneric<List<Integer>, A>>(){}.getSimpleTypeName());
        assertEquals(MyReifiedGenericᐸListᐸIntegerᐳιAᐳ.parameterizedType(),
                     new ReifiedGenericType<MyReifiedGeneric<List<Integer>, A>>(){}.parameterizedType());
        assertEquals("MyReifiedGeneric<Map<String,List<Double>>,B>", MyReifiedGenericᐸMapᐸStringιListᐸDoubleᐳᐳιBᐳᐳ.getSimpleTypeName());
        
        MyReifiedGeneric<List<Integer>, A> reifiedGeneric1 = buildReifiedGeneric(new ArrayList<>(), (A) new B());
        MyReifiedGeneric<Map<String,List<Double>>,B> reifiedGeneric2 = buildReifiedGeneric(new HashMap<>(), new B(), "", 0.0);
        
        assertEquals("MyReifiedGenericImpl1<List<Integer>,A,String,Double>", reifiedGeneric1.reifiedGenericType().getSimpleTypeName());
        assertEquals("MyReifiedGenericImpl2<Map<String,List<Double>>,B,Double,String>", reifiedGeneric2.reifiedGenericType().getSimpleTypeName());
        
        assertTrue(MyReifiedGenericImpl1ᐸListᐸIntegerᐳιAιStringιDoubleᐳ.isInstance(reifiedGeneric1));
        assertFalse(MyReifiedGenericᐸListᐸIntegerᐳιAᐳ.isInstance(reifiedGeneric2));
        assertTrue(new ReifiedGenericType<MyReifiedGenericImpl2<Map<String, List<Double>>, B, Double, String>>(){}.isInstance(reifiedGeneric2));
        
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
            assertEquals("MyReifiedGenericImpl1<List<Integer>,A,String,Double> is not castable to MyReifiedGeneric<Map<String,List<Double>>,B>", e.getMessage());
        }
    }
}
