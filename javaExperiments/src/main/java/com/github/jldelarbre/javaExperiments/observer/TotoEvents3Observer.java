package com.github.jldelarbre.javaExperiments.observer;

public class TotoEvents3Observer implements ITotoEvents3Observer, ITotosEvents3 {

    @Override
    public void eventCommon() {
        System.out.println("TotoEvents3Observer.eventCommon()");
    }

    @Override
    public void eventAa3() {
        System.out.println("TotoEvents3Observer.eventAa3()");
    }

    @Override
    public ITotosEvents3 process() {
        return this;
    }

    @Override
    public Class<ITotosEvents3> getObservedEventsType() {
        return ITotosEvents3.class;
    }
}
