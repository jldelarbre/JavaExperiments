package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import com.github.jldelarbre.javaExperiments.observer.observer.IEventRaiser;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservablesEvents;

public interface IIEventRaiserProxy<ObservablesEventsType extends IObservablesEvents>
        extends IEventRaiser<ObservablesEventsType> {

    IEventRaiser<ObservablesEventsType> getRaiser();

    @Override
    default ObservablesEventsType raise() {
        return getRaiser().raise();
    }
}
