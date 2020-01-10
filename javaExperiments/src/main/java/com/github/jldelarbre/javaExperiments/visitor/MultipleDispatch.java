package com.github.jldelarbre.javaExperiments.visitor;
import java.util.HashMap;
import java.util.Map;

public class MultipleDispatch {

    // Visitable: Object on which we will apply a multiple dispatch
    interface ObjectType {
        // accept
        void performOperation(OperationType operation);

        interface Operable extends ObjectType {
            ObjectData getData();

            interface ObjectData {}
        }
        
        interface OperableGeneric<O extends OperableGeneric<O>> extends ObjectType.Operable {
        }
    }

    interface ObjectType1 extends ObjectType.OperableGeneric<ObjectType1> {
        @Override
        public Data getData();

        public class Data implements ObjectData {}
    }

    static class ObjectType1Impl implements ObjectType1 {

        private ObjectType1.Data data;

        @Override
        public void performOperation(OperationType operation) {
            operation.doOperationOn(this);
        }

        @Override
        public ObjectType1.Data getData() {
            return this.data;
        }
    }

    interface ObjectType2 extends ObjectType.OperableGeneric<ObjectType2> {
        @Override
        public Data getData();

        public class Data implements ObjectData {}
    }

    static class ObjectType2Impl implements ObjectType2 {

        private ObjectType2.Data data;

        @Override
        public void performOperation(OperationType operation) {
            operation.doOperationOn(this);
        }

        @Override
        public ObjectType2.Data getData() {
            return this.data;
        }
    }

    // Visitor: object that holds processing that will be applied after dispatch
    interface OperationType {
        // visit
        void doOperationOn(ObjectType object);
        
        interface Generic<O extends ObjectType.OperableGeneric<O>> {
            void doOperationOn(O object);
        }
    }

    interface OperationType1 extends OperationType {
        public void doOperationOn(ObjectType1 object);
        public void doOperationOn(ObjectType2 object);
        
        interface Generic<O extends ObjectType.OperableGeneric<O>> extends OperationType.Generic<O> {
        }
    }

    static class OperationType1Impl implements OperationType1 {

        private final Map<Class<? extends ObjectType>, OperationType1.Generic<?>> objectTypeMap = new HashMap<>();

        public OperationType1Impl() {
            this.objectTypeMap.put(ObjectType1Impl.class, new OperationType1OnObjectType1Impl());
            this.objectTypeMap.put(ObjectType2Impl.class, new OperationType1OnObjectType2Impl());
        }

        @Override
        public void doOperationOn(ObjectType object) {
            throw new UnsupportedOperationException("No " + OperationType1.class.getSimpleName()
                                                    + " implementation for " + object.getClass().getSimpleName());
        }
        
        @Override
        public void doOperationOn(ObjectType1 object) {
            @SuppressWarnings("unchecked")
            final OperationType1.Generic<ObjectType1> operationType2 = (OperationType1.Generic<ObjectType1>) this.objectTypeMap.get(object.getClass());
            if (operationType2 == null) {
                throw new UnsupportedOperationException("No " + OperationType1.class.getSimpleName()
                                                        + " implementation for " + object.getClass().getSimpleName());
            }
            operationType2.doOperationOn(object);
        }
        
        @Override
        public void doOperationOn(ObjectType2 object) {
            @SuppressWarnings("unchecked")
            final OperationType1.Generic<ObjectType2> operationType2 = (OperationType1.Generic<ObjectType2>) this.objectTypeMap.get(object.getClass());
            if (operationType2 == null) {
                throw new UnsupportedOperationException("No " + OperationType1.class.getSimpleName()
                                                        + " implementation for " + object.getClass().getSimpleName());
            }
            operationType2.doOperationOn(object);
        }
    }

    interface OperationType2 extends OperationType {
        public void doOperationOn(ObjectType1 object);
        public void doOperationOn(ObjectType2 object);
        
        interface Generic<O extends ObjectType.OperableGeneric<O>> extends OperationType.Generic<O> {
        }
    }

    static class OperationType2Impl implements OperationType2 {

        private final Map<Class<? extends ObjectType>, OperationType2.Generic<?>> objectTypeMap = new HashMap<>();

        public OperationType2Impl() {
            this.objectTypeMap.put(ObjectType1Impl.class, new OperationType2OnObjectType1Impl());
            this.objectTypeMap.put(ObjectType2Impl.class, new OperationType2OnObjectType2Impl());
        }

        @Override
        public void doOperationOn(ObjectType object) {
            throw new UnsupportedOperationException("No " + OperationType2.class.getSimpleName()
                                                    + " implementation for " + object.getClass().getSimpleName());
        }
        
        @Override
        public void doOperationOn(ObjectType1 object) {
            @SuppressWarnings("unchecked")
            final OperationType2.Generic<ObjectType1> operationType2 = (OperationType2.Generic<ObjectType1>) this.objectTypeMap.get(object.getClass());
            if (operationType2 == null) {
                throw new UnsupportedOperationException("No " + OperationType2.class.getSimpleName()
                                                        + " implementation for " + object.getClass().getSimpleName());
            }
            operationType2.doOperationOn(object);
        }
        
        @Override
        public void doOperationOn(ObjectType2 object) {
            @SuppressWarnings("unchecked")
            final OperationType2.Generic<ObjectType2> operationType2 = (OperationType2.Generic<ObjectType2>) this.objectTypeMap.get(object.getClass());
            if (operationType2 == null) {
                throw new UnsupportedOperationException("No " + OperationType2.class.getSimpleName()
                                                        + " implementation for " + object.getClass().getSimpleName());
            }
            operationType2.doOperationOn(object);
        }
    }

    interface OperationType1OnObjectType1 extends OperationType1.Generic<ObjectType1> {}

    static class OperationType1OnObjectType1Impl implements OperationType1OnObjectType1 {

        // More specific parameters possible here

        public void doOperationOn(ObjectType1 object) {
            System.out.println("MultipleDispatch.OperationType1OnObjectType1Impl.doParametrizedOperationOn()");
            @SuppressWarnings("unused")
            final ObjectType1.Data data = object.getData();
            // Do something
        }
    }

    interface OperationType1OnObjectType2 extends OperationType1.Generic<ObjectType2> {}

    static class OperationType1OnObjectType2Impl implements OperationType1OnObjectType2 {

        // More specific parameters possible here

        @Override
        public void doOperationOn(ObjectType2 object) {
            System.out.println("MultipleDispatch.OperationType1OnObjectType2Impl.doParametrizedOperationOn()");
            @SuppressWarnings("unused")
            final ObjectType2.Data data = object.getData();
            // Do something
        }
    }

    interface OperationType2OnObjectType1 extends OperationType2.Generic<ObjectType1> {}

    static class OperationType2OnObjectType1Impl implements OperationType2OnObjectType1 {

        // More specific parameters possible here

        @Override
        public void doOperationOn(ObjectType1 object) {
            System.out.println("MultipleDispatch.OperationType2OnObjectType1Impl.doParametrizedOperationOn()");
            @SuppressWarnings("unused")
            final ObjectType1.Data data = object.getData();
            // Do something
        }
    }

    interface OperationType2OnObjectType2 extends OperationType2.Generic<ObjectType2> {}

    static class OperationType2OnObjectType2Impl implements OperationType2OnObjectType2 {

        // More specific parameters possible here

        @Override
        public void doOperationOn(ObjectType2 object) {
            System.out.println("MultipleDispatch.OperationType2OnObjectType2Impl.doParametrizedOperationOn()");
            @SuppressWarnings("unused")
            final ObjectType2.Data data = object.getData();
            // Do something
        }
    }

    public static void main(String[] args) {

        final ObjectType1 objectType1A = new ObjectType1Impl();
        final ObjectType objectTypeA = objectType1A;
        final ObjectType2 objectType2B = new ObjectType2Impl();
        final ObjectType objectTypeB = objectType2B;

        final OperationType1 operationType1A = new OperationType1Impl();
        final OperationType operationTypeA = operationType1A;
        final OperationType2 operationType2B = new OperationType2Impl();
        final OperationType operationTypeB = operationType2B;

        objectTypeA.performOperation(operationTypeA);
        objectTypeA.performOperation(operationTypeB);
        objectTypeB.performOperation(operationTypeA);
        objectTypeB.performOperation(operationTypeB);
    }
}
