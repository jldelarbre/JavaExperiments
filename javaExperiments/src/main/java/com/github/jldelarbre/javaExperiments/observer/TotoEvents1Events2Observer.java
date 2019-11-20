package com.github.jldelarbre.javaExperiments.observer;

public class TotoEvents1Events2Observer implements ITotoEvents1Events2Observer {

    public static class InnerObserverImpl implements IObservableTotosEvents1Events2 {
        @Override
        public void eventCommon() {
            System.out.println("TotoEvents1Events2Observer.eventCommon()");
        }

        @Override
        public void eventYoupla1() {
            System.out.println("TotoEvents1Events2Observer.eventYoupla1()");
        }

        @Override
        public void eventYouplaBoom1(String someParam) {
            System.out.println("TotoEvents1Events2Observer.eventYouplaBoom1(" + someParam + ")");
        }

        @Override
        public void eventYoupla2() {
            System.out.println("TotoEvents1Events2Observer.eventYoupla2()");
        }

        @Override
        public void eventYouplaBoom2(String someParam) {
            System.out.println("TotoEvents1Events2Observer.eventYouplaBoom2(" + someParam + ")");
        }

        @Override
        public void eventYoupla1(int kiki, Integer titi) {
            System.out.println("TotoEvents1Events2Observer.InnerObserverImpl.eventYoupla1(" + kiki + ", " + titi + ")");
        }

        @Override
        public void eventYouplaBoom1(Integer bibi) {
            System.out.println("TotoEvents1Events2Observer.InnerObserverImpl.eventYouplaBoom1(" + bibi + ")");
        }
    }

    @Override
    public IObservableTotosEvents1Events2 process() {
        return new InnerObserverImpl();
    }

    @Override
    public Class<IObservableTotosEvents1Events2> getObservedEventsType() {
        return IObservableTotosEvents1Events2.class;
    }
}
