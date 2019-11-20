package com.github.jldelarbre.javaExperiments.observer;

public class TotoEvents3Observer implements ITotoEvents3Observer, IObservableTotosEvents3 {

    @Override
    public void eventCommon() {
        System.out.println("TotoEvents3Observer.eventCommon()");
    }

    @Override
    public void eventAa3() {
        System.out.println("TotoEvents3Observer.eventAa3()");
    }

    @Override
    public IObservableTotosEvents3 process() {
        return this;
    }

    @Override
    public Class<IObservableTotosEvents3> getObservedEventsType() {
        return IObservableTotosEvents3.class;
    }
}
