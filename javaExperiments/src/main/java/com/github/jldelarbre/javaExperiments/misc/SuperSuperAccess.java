package com.github.jldelarbre.javaExperiments.misc;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

public class SuperSuperAccess {

    static class AA {
        int a = 15;
        @Override
        public String toString() {
            return "AA_" + a + "_" + super.toString();
        }
    }
    
    static class BB extends AA {
        int b = 16;
        @Override
        public String toString() {
            return "BB_" + b + "_" + a + "_" + super.toString();
        }
    }

    public static void main(String[] args) throws Throwable {
        BB b = new BB();

        System.out.println(b);
        
        Field IMPL_LOOKUP = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
        IMPL_LOOKUP.setAccessible(true);
        MethodHandles.Lookup lkp = (MethodHandles.Lookup) IMPL_LOOKUP.get(null);
        
        MethodHandle h1 = lkp.findSpecial(AA.class, "toString", MethodType.methodType(String.class), AA.class);
        System.out.println(h1.invoke(b));
        
        MethodHandle h2 = lkp.findSpecial(Object.class, "toString", MethodType.methodType(String.class), Object.class);
        System.out.println(h2.invoke(b));
    }
}
