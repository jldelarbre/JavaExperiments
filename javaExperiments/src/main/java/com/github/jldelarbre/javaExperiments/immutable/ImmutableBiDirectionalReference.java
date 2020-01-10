package com.github.jldelarbre.javaExperiments.immutable;
public class ImmutableBiDirectionalReference {

    static final class A {
        private final String aStr;
        private final B b;

        public static A build(String aStr, B.Builder bBuilder) {
            return new A(aStr, bBuilder);
        }

        private A(String aStr, B.Builder bBuilder) {
            this.aStr = aStr;
            this.b = bBuilder.build(this);
        }

        public String getAStr() {
            return this.aStr;
        }

        public B getB() {
            return this.b;
        }
    }

    static class B {
        private final String bStr;
        private final A a;

        private B(String bStr, A a) {
            this.bStr = bStr;
            this.a = a;
        }

        public String getBStr() {
            return this.bStr;
        }

        public A getA() {
            return this.a;
        }

        static class Builder {
            private final String bStr;

            public Builder(String bStr) {
                this.bStr = bStr;
            }

            public B build(A a) {
                return new B(this.bStr, a);
            }
        }
    }

    public static void main(String[] args) {
        final B.Builder bBuilder = new B.Builder("someBString");
        final A a = A.build("someAString", bBuilder);
        final B b = a.getB();

        System.out.println("a.aStr = " + a.getAStr());
        System.out.println("b.bStr = " + b.getBStr());
        System.out.println("a.aStr from b = " + b.getA().getAStr());
        System.out.println("b.bStr from a = " + a.getB().getBStr());
    }
}
