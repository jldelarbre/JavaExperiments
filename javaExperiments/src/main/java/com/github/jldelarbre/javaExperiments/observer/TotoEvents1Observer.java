package com.github.jldelarbre.javaExperiments.observer;

public class TotoEvents1Observer implements ITotoEvents1Observer, ITotosEvents1 {

    private final String observerDescription = "observer event 1";

    @Override
    public void eventCommon() {
        System.out.println("TotoEvents1Observer.eventCommon()");
    }

    @Override
    public void eventYoupla1() {
        System.out.println("TotoEvents1Observer.eventYoupla1()");
    }

    @Override
    public void eventYouplaBoom1(String someParam) {
        System.out.println("TotoEvents1Observer.eventYouplaBoom1(" + someParam + ")");
    }

    @Override
    public ITotosEvents1 process() {
        return this;
    }

    @Override
    public Class<ITotosEvents1> getObservedEventsType() {
        return ITotosEvents1.class;
    }

    @Override
    public String toString() {
        return this.observerDescription + this.getObserverDescription();
    }
}
