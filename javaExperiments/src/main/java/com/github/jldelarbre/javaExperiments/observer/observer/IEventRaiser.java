package com.github.jldelarbre.javaExperiments.observer.observer;

public interface IEventRaiser<ObservablesEventsType extends IObservablesEvents> {

    ObservablesEventsType raise();
    // raise_async
    // raise_sync

    interface IProxy<ObservablesEventsType extends IObservablesEvents> extends IEventRaiser<ObservablesEventsType> {

        IEventRaiser<ObservablesEventsType> getRaiser();

        @Override
        default ObservablesEventsType raise() {
            return getRaiser().raise();
        }
    }
}
