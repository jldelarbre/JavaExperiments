package com.github.jldelarbre.javaExperiments.visitor;
import java.util.HashMap;
import java.util.Map;

public class MultipleDispatchWithParam {

    // Visitable: Object on which we will apply a multiple dispatch
    interface ObjectType {
        // accept
        OperationType.Result performOperation(OperationType operation, OperationType.Parameter param);

        <P extends OperationType.Parameter, R extends OperationType.Result> R
            performOperation(OperationType.Full<P, R> operation, P param);

        interface Operable extends ObjectType {
            ObjectData getData();

            interface ObjectData {
            }
        }
    }

    interface ObjectType1 extends ObjectType.Operable {
        @Override
        public Data getData();

        public class Data implements ObjectData {
        }
    }

    static class ObjectType1Impl implements ObjectType1 {

        private ObjectType1.Data data;

        @Override
        public OperationType.Result performOperation(OperationType operation, OperationType.Parameter param) {
            return operation.doOperationOn(this, param);
        }

        @Override
        public <P extends OperationType.Parameter, R extends OperationType.Result> R
            performOperation(OperationType.Full<P, R> operation, P param) {
            return operation.doParametrizedOperationOn(this, param);
        }

        @Override
        public ObjectType1.Data getData() {
            return this.data;
        }
    }

    interface ObjectType2 extends ObjectType.Operable {
        @Override
        public Data getData();

        public class Data implements ObjectData {
        }
    }

    static class ObjectType2Impl implements ObjectType2 {

        private ObjectType2.Data data;

        @Override
        public OperationType.Result performOperation(OperationType operation, OperationType.Parameter param) {
            return operation.doOperationOn(this, param);
        }

        @Override
        public <P extends OperationType.Parameter, R extends OperationType.Result> R
            performOperation(OperationType.Full<P, R> operation, P param) {
            return operation.doParametrizedOperationOn(this, param);
        }

        @Override
        public ObjectType2.Data getData() {
            return this.data;
        }
    }

    // Visitor: object that holds processing that will be applied after dispatch
    interface OperationType {
        // visit
        Result doOperationOn(ObjectType object, Parameter parameter);

        interface Parameter {
        }

        interface Result {
        }

        interface Parametrized<P extends OperationType.Parameter, R extends OperationType.Result> {
            R doParametrizedOperationOn(ObjectType object, P parameter);
        }

        interface Full<P extends OperationType.Parameter, R extends OperationType.Result>
                extends OperationType, OperationType.Parametrized<P, R> {
        }
    }

    interface OperationType1 extends OperationType.Full<OperationType1.Parameter, OperationType1.Result> {
        @Override
        OperationType1.Result doOperationOn(ObjectType object, OperationType.Parameter parameter);

        class Parameter implements OperationType.Parameter {
        }

        class Result implements OperationType.Result {
        }

        interface Parametrized extends OperationType.Parametrized<OperationType1.Parameter, OperationType1.Result> {
        }
    }

    static class OperationType1Impl implements OperationType1 {

        private final Map<Class<? extends ObjectType>, OperationType1.Parametrized> objectTypeMap = new HashMap<>();

        public OperationType1Impl() {
            this.objectTypeMap.put(ObjectType1Impl.class, new OperationType1OnObjectType1Impl());
            this.objectTypeMap.put(ObjectType2Impl.class, new OperationType1OnObjectType2Impl());
        }

        @Override
        public OperationType1.Result doOperationOn(ObjectType object, OperationType.Parameter parameter) {
            if (!(parameter instanceof OperationType1.Parameter)) {
                throw new ClassCastException("Expect " + OperationType1.Parameter.class.getSimpleName() + " with "
                        + OperationType1.class.getSimpleName());
            }
            return this.doParametrizedOperationOn(object, (OperationType1.Parameter) parameter);
        }

        @Override
        public OperationType1.Result doParametrizedOperationOn(ObjectType object, OperationType1.Parameter parameter) {
            final OperationType1.Parametrized operationType1 = this.objectTypeMap.get(object.getClass());
            if (operationType1 == null) {
                throw new UnsupportedOperationException("No " + OperationType1.class.getSimpleName()
                        + " implementation for " + object.getClass().getSimpleName());
            }
            return operationType1.doParametrizedOperationOn(object, parameter);
        }
    }

    interface OperationType2 extends OperationType.Full<OperationType2.Parameter, OperationType2.Result> {
        @Override
        OperationType2.Result doOperationOn(ObjectType object, OperationType.Parameter parameter);

        class Parameter implements OperationType.Parameter {
        }

        class Result implements OperationType.Result {
        }

        interface Parametrized extends OperationType.Parametrized<OperationType2.Parameter, OperationType2.Result> {
        }
    }

    static class OperationType2Impl implements OperationType2 {

        private final Map<Class<? extends ObjectType>, OperationType2.Parametrized> objectTypeMap = new HashMap<>();

        public OperationType2Impl() {
            this.objectTypeMap.put(ObjectType1Impl.class, new OperationType2OnObjectType1Impl());
            this.objectTypeMap.put(ObjectType2Impl.class, new OperationType2OnObjectType2Impl());
        }

        @Override
        public OperationType2.Result doOperationOn(ObjectType object, OperationType.Parameter parameter) {
            if (!(parameter instanceof OperationType2.Parameter)) {
                throw new ClassCastException("Expect " + OperationType2.Parameter.class.getSimpleName() + " with "
                        + OperationType2.class.getSimpleName());
            }
            return this.doParametrizedOperationOn(object, (OperationType2.Parameter) parameter);
        }

        @Override
        public OperationType2.Result doParametrizedOperationOn(ObjectType object, OperationType2.Parameter parameter) {
            final OperationType2.Parametrized operationType2 = this.objectTypeMap.get(object.getClass());
            if (operationType2 == null) {
                throw new UnsupportedOperationException("No " + OperationType2.class.getSimpleName()
                        + " implementation for " + object.getClass().getSimpleName());
            }
            return operationType2.doParametrizedOperationOn(object, parameter);
        }
    }

    interface OperationType1OnObjectType1 extends OperationType1.Parametrized {
    }

    static class OperationType1OnObjectType1Impl implements OperationType1OnObjectType1 {

        // More specific parameters possible here

        @Override
        public OperationType1.Result doParametrizedOperationOn(ObjectType object, OperationType1.Parameter parameter) {
            if (!(object instanceof ObjectType1)) {
                throw new ClassCastException("Expect " + ObjectType1.class.getSimpleName() + " with "
                        + OperationType1.class.getSimpleName());
            }
            System.out.println("MultipleDispatch.OperationType1OnObjectType1Impl.doParametrizedOperationOn()");
            final ObjectType1 typedObject = (ObjectType1) object;
            @SuppressWarnings("unused")
            final ObjectType1.Data data = typedObject.getData();
            // Do something
            return new OperationType1.Result();
        }
    }

    interface OperationType1OnObjectType2 extends OperationType1.Parametrized {
    }

    static class OperationType1OnObjectType2Impl implements OperationType1OnObjectType2 {

        // More specific parameters possible here

        @Override
        public OperationType1.Result doParametrizedOperationOn(ObjectType object, OperationType1.Parameter parameter) {
            if (!(object instanceof ObjectType2)) {
                throw new ClassCastException("Expect " + ObjectType2.class.getSimpleName() + " with "
                        + OperationType1.class.getSimpleName());
            }
            System.out.println("MultipleDispatch.OperationType1OnObjectType2Impl.doParametrizedOperationOn()");
            final ObjectType2 typedObject = (ObjectType2) object;
            @SuppressWarnings("unused")
            final ObjectType2.Data data = typedObject.getData();
            // Do something
            return new OperationType1.Result();
        }
    }

    interface OperationType2OnObjectType1 extends OperationType2.Parametrized {
    }

    static class OperationType2OnObjectType1Impl implements OperationType2OnObjectType1 {

        // More specific parameters possible here

        @Override
        public OperationType2.Result doParametrizedOperationOn(ObjectType object, OperationType2.Parameter parameter) {
            if (!(object instanceof ObjectType1)) {
                throw new ClassCastException("Expect " + ObjectType1.class.getSimpleName() + " with "
                        + OperationType1.class.getSimpleName());
            }
            System.out.println("MultipleDispatch.OperationType2OnObjectType1Impl.doParametrizedOperationOn()");
            final ObjectType1 typedObject = (ObjectType1) object;
            @SuppressWarnings("unused")
            final ObjectType1.Data data = typedObject.getData();
            // Do something
            return new OperationType2.Result();
        }
    }

    interface OperationType2OnObjectType2 extends OperationType2.Parametrized {
    }

    static class OperationType2OnObjectType2Impl implements OperationType2OnObjectType2 {

        // More specific parameters possible here

        @Override
        public OperationType2.Result doParametrizedOperationOn(ObjectType object, OperationType2.Parameter parameter) {
            if (!(object instanceof ObjectType2)) {
                throw new ClassCastException("Expect " + ObjectType2.class.getSimpleName() + " with "
                        + OperationType2.class.getSimpleName());
            }
            System.out.println("MultipleDispatch.OperationType2OnObjectType2Impl.doParametrizedOperationOn()");
            final ObjectType2 typedObject = (ObjectType2) object;
            @SuppressWarnings("unused")
            final ObjectType2.Data data = typedObject.getData();
            // Do something
            return new OperationType2.Result();
        }
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {

        final ObjectType1 objectType1A = new ObjectType1Impl();
        final ObjectType objectTypeA = objectType1A;
        final ObjectType2 objectType2B = new ObjectType2Impl();
        final ObjectType objectTypeB = objectType2B;

        final OperationType1 operationType1A = new OperationType1Impl();
        final OperationType operationTypeA = operationType1A;
        final OperationType2 operationType2B = new OperationType2Impl();
        final OperationType operationTypeB = operationType2B;

        final OperationType1.Parameter operationType1Param = new OperationType1.Parameter() {
        };
        final OperationType.Parameter operationParamA = operationType1Param;
        final OperationType2.Parameter operationType2Param = new OperationType2.Parameter() {
        };
        final OperationType.Parameter operationParamB = operationType2Param;

        final OperationType.Result result1A_A = objectTypeA.performOperation(operationTypeA, operationParamA);
        final OperationType.Result result1A_B = objectTypeA.performOperation(operationTypeB, operationParamB);
        final OperationType.Result result1B_A = objectTypeB.performOperation(operationTypeA, operationParamA);
        final OperationType.Result result1B_B = objectTypeB.performOperation(operationTypeB, operationParamB);
    }
}
