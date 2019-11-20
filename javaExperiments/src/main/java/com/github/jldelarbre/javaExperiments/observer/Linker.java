
package com.github.jldelarbre.javaExperiments.observer;

public class Linker {

    public static void main(String[] args) {
        final ITotoEvents1Observer observer1 = new TotoEvents1Observer();
        final ITotoEvents1Events2Observer observer1_2 = new TotoEvents1Events2Observer();
        final ITotoEvents3Observer observer3 = new TotoEvents3Observer();

        final IToto toto = Toto.build();
        toto.manageTotoEvents1Observable().addObserver(observer1);
        toto.addObserver(observer1_2);
        toto.manageTotoEvents3Observable().addObserver(observer3);

        toto.f();
        System.out.println();
        toto.manageTotoEvents1Observable().disable();
        toto.f();
        System.out.println();
        toto.manageTotoEvents1Observable().enable();
        toto.f();
    }
}
