package com.github.jldelarbre.javaExperiments.observer.observer;

public interface IObserver<ObservablesEventsType extends IObservablesEvents> {
    ObservablesEventsType process();

    Class<ObservablesEventsType> getObservablesEventsType();

    default String getObserverDescription() {
        return getObservablesEventsType().getSimpleName() + " observer";
    }
}
