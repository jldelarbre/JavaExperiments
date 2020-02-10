
package com.github.jldelarbre.javaExperiments.observer;

public class Linker {

    public static void main(String[] args) {
        final ITotoEvents1Observer observer1 = new TotoEvents1Observer();
        final ITotoEvents1Events2Observer observer1_2 = new TotoEvents1Events2Observer();
        final ITotoEvents3Observer observer3 = new TotoEvents3Observer();

        final IToto toto = Toto.build();
        toto.manageTotoEvents1Observable().addObserver(observer1);
        toto.manageTotoEvents1Observable().addObserver(observer1_2);
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
    
    // Expected output:
    
//    TotoEvents1Observer.eventYouplaBoom1(yolo)
//    TotoEvents1Events2Observer.eventYouplaBoom1(yolo)
//    TotoEvents1Events2Observer.InnerObserverImpl.eventYouplaBoom1(42)
//    TotoEvents1Events2Observer.InnerObserverImpl.eventYoupla1(77, 37)
//    TotoEvents1Events2Observer.eventYoupla2()
//    TotoEvents1Observer.eventCommon()
//    TotoEvents1Events2Observer.eventCommon()
//    TotoEvents3Observer.eventAa3()
//    TotoEvents3Observer.eventCommon()
//
//    TotoEvents1Events2Observer.eventYouplaBoom1(yolo)
//    TotoEvents1Events2Observer.InnerObserverImpl.eventYouplaBoom1(42)
//    TotoEvents1Events2Observer.InnerObserverImpl.eventYoupla1(77, 37)
//    TotoEvents1Events2Observer.eventYoupla2()
//    TotoEvents1Events2Observer.eventCommon()
//    TotoEvents3Observer.eventAa3()
//    TotoEvents3Observer.eventCommon()
//
//    TotoEvents1Observer.eventYouplaBoom1(yolo)
//    TotoEvents1Events2Observer.eventYouplaBoom1(yolo)
//    TotoEvents1Events2Observer.InnerObserverImpl.eventYouplaBoom1(42)
//    TotoEvents1Events2Observer.InnerObserverImpl.eventYoupla1(77, 37)
//    TotoEvents1Events2Observer.eventYoupla2()
//    TotoEvents1Observer.eventCommon()
//    TotoEvents1Events2Observer.eventCommon()
//    TotoEvents3Observer.eventAa3()
//    TotoEvents3Observer.eventCommon()
}
