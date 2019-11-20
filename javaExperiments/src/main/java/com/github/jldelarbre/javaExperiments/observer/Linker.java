package com.github.jldelarbre.javaExperiments.observer;

public class Linker {

    public static void main(String[] args) {
        final ITotoEvents1Observer observer1 = new TotoEvents1Observer();
        final ITotoEvents1Events2Observer observer2 = new TotoEvents1Events2Observer();
        final ITotoEvents3Observer observer3 = new TotoEvents3Observer();

        final IToto toto = Toto.build();
        toto.manageObserverTotoEvents1().addObserver(observer1);
        toto.addObserver(observer2);
        toto.manageObserverTotoEvents3().addObserver(observer3);

        toto.f();
        System.out.println();
        toto.manageObserverTotoEvents1().disableObservers();
        toto.f();
        System.out.println();
        toto.manageObserverTotoEvents1().enableObservers();
        toto.f();
    }
}
