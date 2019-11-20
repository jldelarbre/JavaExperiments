package com.github.jldelarbre.javaExperiments.observer.observer;

public interface IObserver<ObservablesEventsType extends IObservablesEvents> {
    ObservablesEventsType process();

    Class<ObservablesEventsType> getObservedEventsType();

    default String getObserverDescription() {
        return getObservedEventsType().getSimpleName() + " observer";
    }
}
