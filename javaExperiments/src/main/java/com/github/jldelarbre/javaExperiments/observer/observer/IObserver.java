package com.github.jldelarbre.javaExperiments.observer.observer;

public interface IObserver<EventsType extends IEvents> {
    EventsType process();

    Class<EventsType> getObservedEventsType();

    default String getObserverDescription() {
        return getObservedEventsType().getSimpleName() + " observer";
    }
}
