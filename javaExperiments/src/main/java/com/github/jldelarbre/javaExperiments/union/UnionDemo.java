package com.github.jldelarbre.javaExperiments.union;

import static com.github.jldelarbre.javaExperiments.union.UnionDemo.MyUnionTypes.DOUBLE;
import static com.github.jldelarbre.javaExperiments.union.UnionDemo.MyUnionTypes.NUMBER;
import static com.github.jldelarbre.javaExperiments.union.UnionDemo.MyUnionTypes.STRING;
import static com.github.jldelarbre.javaExperiments.union.UnionDemo.MyUnionTypes.STRING2;
import static com.github.jldelarbre.javaExperiments.union.UnionDemo.MyNumberUnionTypes.NUM_DOUBLE;
import static com.github.jldelarbre.javaExperiments.union.UnionDemo.MyNumberUnionTypes.NUM_INTEGER;

public class UnionDemo {

    interface Union<CommonSuperType, UnionType extends Union.Type<CommonSuperType>> {

        UnionType getActiveUnionType();

        <ActiveType extends CommonSuperType> ActiveType getValue(UnionType activeEnumType, Class<ActiveType> activeType);
        
        interface Type<CommonSuperType> {
            Class<? extends CommonSuperType> getType();
        }
        
        interface Obj<UnionType extends Union.Type<Object>> extends Union<Object, UnionType> {
        }
    }
    
    
    public static final class UnionFactory {
        public static <CommonSuperType, UnionType extends Union.Type<CommonSuperType>>
        Union<CommonSuperType, UnionType> buildTyped(UnionType unionType, CommonSuperType value) {
            if (!unionType.getType().isAssignableFrom(value.getClass())) {
                throw new ClassCastException("Type of value \"" + value.getClass().getSimpleName() +
                    "\" to store is not assignable to enumType \"" +
                    unionType.getType().getSimpleName() + "\"");
            }
            
            @SuppressWarnings("unchecked")
            Union.Type<Object> untypedUnionType = (Union.Type<Object>) unionType;
            UnionImpl<Union.Type<Object>> unionImpl = new UnionImpl<>(untypedUnionType, value);
            @SuppressWarnings("unchecked")
            Union<CommonSuperType, UnionType> castUnionImpl = (Union<CommonSuperType, UnionType>) unionImpl;
            
            return castUnionImpl;
        }
        
        public static <UnionType extends Union.Type<Object>>
        Union.Obj<UnionType> build(UnionType unionType, Object value) {
            Union<Object, UnionType> union = buildTyped(unionType, value);
            
            return (Union.Obj<UnionType>) union;
        }
    }
    
    static final class UnionImpl<UnionType extends Union.Type<Object>>
        implements Union.Obj<UnionType> {
    
        private final UnionType activeUnionType;
        private final Object value;
    
        UnionImpl(UnionType activeEnumType, Object value) {
            this.activeUnionType = activeEnumType;
            this.value = value;
        }
    
        @Override
        public UnionType getActiveUnionType() {
            return this.activeUnionType;
        }
    
        @Override
        public <ActiveType> ActiveType getValue(UnionType pActiveUnionType, Class<ActiveType> activeType) {
            if (!pActiveUnionType.getType().equals(activeType)) {
                throw new ClassCastException("Type to retrieve \"" + activeType.getSimpleName() +
                    "\" does not correspond to activeEnumType \"" +
                    pActiveUnionType.getType().getSimpleName() + "\"");
            }
            if (!this.activeUnionType.equals(pActiveUnionType)) {
                throw new ClassCastException("Erroneous active type (" + pActiveUnionType +
                    ") used to get value in union, current type is \"" + this.activeUnionType + "\"");
            }
            return activeType.cast(this.value);
        }
    }

    static enum MyUnionTypes implements Union.Type<Object> {
        DOUBLE(Double.class),
        STRING(String.class),
        STRING2(String.class),
        NUMBER(Number.class);

        private final Class<?> type;
    
        MyUnionTypes(Class<?> type) {
            this.type = type;
        }
    
        @Override
        public Class<?> getType() {
            return this.type;
        }
    }
    
    static enum MyNumberUnionTypes implements Union.Type<Number> {
        NUM_DOUBLE(Double.class),
        NUM_INTEGER(Integer.class);

        private final Class<? extends Number> type;
    
        MyNumberUnionTypes(Class<? extends Number> type) {
            this.type = type;
        }
    
        @Override
        public Class<? extends Number> getType() {
            return this.type;
        }
    }

    public static void main(String[] args) {
        testUnionObj();
        System.out.println("***************************************");
        testUnion();
    }

    private static void testUnionObj() {
        final Union.Obj<MyUnionTypes> union1 = UnionFactory.build(STRING, "Hello");
        final MyUnionTypes activeUnionType = union1.getActiveUnionType();
        System.out.println(activeUnionType);
        final String value = union1.getValue(STRING, String.class);
        System.out.println(value);
        final Object value2 = union1.getValue(STRING, STRING.getType());
        System.out.println(value2);

        final Union.Obj<MyUnionTypes> union1b = UnionFactory.build(STRING2, "Hello2");
        final String valueb = union1b.getValue(STRING2, String.class);
        System.out.println(valueb);
        try {
            union1b.getValue(STRING, String.class);
        } catch (final ClassCastException e) {
            System.out.println(e.getMessage());
        }

        final Union.Obj<MyUnionTypes> union2 = UnionFactory.build(NUMBER, 42);
        System.out.println(union2.getActiveUnionType());
        System.out.println(union2.getValue(NUMBER, Number.class));

        try {
            union2.getValue(NUMBER, Integer.class);
        } catch (final ClassCastException e) {
            System.out.println(e.getMessage());
        }

        try {
            UnionFactory.buildTyped(DOUBLE, "aa");
        } catch (final ClassCastException e) {
            System.out.println(e.getMessage());
        }

        try {
            UnionFactory.buildTyped(DOUBLE, 75);
        } catch (final ClassCastException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static void testUnion() {
        final Union<Number, MyNumberUnionTypes> union1 = UnionFactory.buildTyped(NUM_DOUBLE, 0.);
        final MyNumberUnionTypes activeUnionType = union1.getActiveUnionType();
        System.out.println(activeUnionType);
        final Double value = union1.getValue(NUM_DOUBLE, Double.class);
        System.out.println(value);
        final Object value2 = union1.getValue(NUM_DOUBLE, NUM_DOUBLE.getType());
        System.out.println(value2);
        
        final Union<Number, MyNumberUnionTypes> union2 = UnionFactory.buildTyped(NUM_INTEGER, 42);
        System.out.println(union2.getActiveUnionType());
        System.out.println(union2.getValue(NUM_INTEGER, Integer.class));
        
        try {
            union2.getValue(NUM_INTEGER, Double.class);
        } catch (final ClassCastException e) {
            System.out.println(e.getMessage());
        }
        
        try {
            UnionFactory.buildTyped(NUM_INTEGER, 3.2);
        } catch (final ClassCastException e) {
            System.out.println(e.getMessage());
        }
        
        try {
            UnionFactory.buildTyped(NUM_DOUBLE, 75);
        } catch (final ClassCastException e) {
            System.out.println(e.getMessage());
        }
    }
}
