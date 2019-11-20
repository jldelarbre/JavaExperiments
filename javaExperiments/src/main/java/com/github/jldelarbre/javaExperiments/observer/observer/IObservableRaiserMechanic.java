package com.github.jldelarbre.javaExperiments.observer.observer;

import com.github.jldelarbre.javaExperiments.observer.observer.internal.IIEventRaiserProxy;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.IObservableProxy;

public interface IObservableRaiserMechanic<ObservablesEventsType extends IObservablesEvents>
        extends IObservableProxy<ObservablesEventsType>, IIEventRaiserProxy<ObservablesEventsType> {

    IObservableRaiserMechanic<ObservablesEventsType> getObservableRaiserMechanic();

    @Override
    default IObservable<ObservablesEventsType> getObservable() {
        return getObservableRaiserMechanic().getObservable();
    }

    @Override
    default IEventRaiser<ObservablesEventsType> getRaiser() {
        return getObservableRaiserMechanic().getRaiser();
    }
}
