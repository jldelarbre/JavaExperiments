package com.github.jldelarbre.javaExperiments.immutable;
public class ImmutableTripletAllReference {

    static final class A {
        private final String aStr;
        private final B b;
        private final C c;

        public static A build(String aStr, BCProvider bcProvider) {
            return new A(aStr, bcProvider);
        }

        private A(String aStr, BCProvider bcProvider) {
            this.aStr = aStr;
            this.b = bcProvider.getB(this);
            this.c = bcProvider.getC(this);
        }

        public String getAStr() {
            return this.aStr;
        }

        public B getB() {
            return this.b;
        }

        public C getC() {
            return this.c;
        }
    }

    static class B {
        private final String bStr;
        private final A a;
        private final C c;

        public static B build(String bStr, A a, C.Builder cBuilder) {
            return new B(bStr, a, cBuilder);
        }

        private B(String bStr, A a, C.Builder cBuilder) {
            this.bStr = bStr;
            this.a = a;
            this.c = cBuilder.build(a, this);
        }

        public String getBStr() {
            return this.bStr;
        }

        public A getA() {
            return this.a;
        }

        public C getC() {
            return this.c;
        }
    }

    static class C {
        private final String cStr;
        private final A a;
        private final B b;

        private C(String cStr, A a, B b) {
            this.cStr = cStr;
            this.a = a;
            this.b = b;
        }

        public String getCStr() {
            return this.cStr;
        }

        public A getA() {
            return this.a;
        }

        public B getB() {
            return this.b;
        }

        static class Builder {
            private final String cStr;

            public Builder(String cStr) {
                this.cStr = cStr;
            }

            public C build(A a, B b) {
                return new C(this.cStr, a, b);
            }
        }
    }

    static class BCProvider {
        private final String bStr;
        private final C.Builder cBuilder;

        private boolean builtDone = false;
        private B b = null;
        private C c = null;

        public static BCProvider build(String bStr, C.Builder cBuilder) {
            return new BCProvider(bStr, cBuilder);
        }

        private BCProvider(String bStr, C.Builder cBuilder) {
            this.bStr = bStr;
            this.cBuilder = cBuilder;
        }

        public B getB(A a) {
            this.buildBC(a);
            return this.b;
        }

        public C getC(A a) {
            this.buildBC(a);
            return this.c;
        }

        private void buildBC(A a) {
            if (this.builtDone) {
                return;
            }

            this.b = B.build(this.bStr, a, this.cBuilder);
            this.c = this.b.getC();

            this.builtDone = true;
        }
    }

    public static void main(String[] args) {
        final C.Builder cBuilder = new C.Builder("someCString");
        final BCProvider bcProvider = BCProvider.build("someBString", cBuilder);
        final A a = A.build("someAString", bcProvider);
        final B b = a.getB();
        final C c = b.getC();

        System.out.println("a.aStr = " + a.getAStr());
        System.out.println("b.bStr = " + b.getBStr());
        System.out.println("c.cStr = " + c.getCStr());

        System.out.println("a = b.a " + a.equals(b.getA()));
        System.out.println("a = c.a " + a.equals(c.getA()));
        System.out.println("b = a.b " + b.equals(a.getB()));
        System.out.println("b = c.b " + b.equals(c.getB()));
        System.out.println("c = a.c " + c.equals(a.getC()));
        System.out.println("c = b.c " + c.equals(b.getC()));
    }
}
