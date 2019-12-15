package com.github.jldelarbre.javaExperiments.traits;

import com.github.jldelarbre.javaExperiments.traits.Traits.TypeTraitComposableImpl.TypeTraitComposableFactory;

public class Traits {

    interface Type {
        void f();
        
        interface Delegate extends Type {
            Type getType();
            
            @Override
            default void f() {
                getType().f();
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    
    static class TypeImpl implements Type {
        @Override
        public void f() {
        }
    }
    
    static class TypeComposer1 implements Type, Type.Delegate {
        private final Type type;
        
        public static Type build() {
            Type type = new TypeImpl();
            return new TypeComposer1(type);
        }
        
        public TypeComposer1(Type type) {
            this.type = type;
        }
        
        @Override
        public Type getType() {
            return type;
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    
    interface TypeTraitSimple extends Type {
        String getData();
        
        @Override
        default void f() {
            getData(); // ...
        }
    }
    
    static class TypeComposer2 implements Type, TypeTraitSimple {
        @Override
        public String getData() {
            return "theData";
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    
    interface TypeTrait {
        interface Data {
            String s();
        }
        
        interface Factory {
            Type build(TypeTrait.Data data);
        }
    }
    
    static class TypeTraitComposableImpl implements Type {
        private final TypeTrait.Data self;
        
        TypeTraitComposableImpl(TypeTrait.Data data) {
            this.self = data;
        }

        @Override
        public void f() {
            self.s();
        }
        
        static class TypeTraitComposableFactory implements TypeTrait.Factory {
            @Override
            public Type build(TypeTrait.Data data) {
                return new TypeTraitComposableImpl(data);
            }
        }
    }
    
    static class TypeComposer3 implements Type, Type.Delegate, TypeTrait.Data {
        
        private final Type type;
        
        public static Type build() {
            TypeTrait.Factory factory = new TypeTraitComposableFactory();
            return new TypeComposer3(factory);
        }
        
        public TypeComposer3(TypeTrait.Factory factory) {
            this.type = factory.build(this);
        }
        
        @Override
        public Type getType() {
            return type;
        }
        
        @Override
        public String s() {
            return "theData";
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    
    interface TypeTraitStatic extends Type, TypeTrait.Data {
        @Override
        default void f() {
            s(); // ...
        }
    }
    
    static class TypeComposer4 implements Type, TypeTraitStatic {
        @Override
        public String s() {
            return "theData";
        }
    }
}
