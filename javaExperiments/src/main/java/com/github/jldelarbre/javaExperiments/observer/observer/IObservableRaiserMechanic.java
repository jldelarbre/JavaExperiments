package com.github.jldelarbre.javaExperiments.observer.observer;

import com.github.jldelarbre.javaExperiments.observer.observer.internal.IEventRaiserDefaultImpl;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.IObservableDefaultImpl;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.IObserverHolder;

public interface IObservableRaiserMechanic<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
        extends IObservableDefaultImpl<ObserverType, ObservablesEventsType>,
        IEventRaiserDefaultImpl<ObservablesEventsType> {

    IObservableRaiserMechanic<ObserverType, ObservablesEventsType> getObservableRaiserMechanic();

    default IObservable<ObserverType, ObservablesEventsType> getObservable() {
        return getObservableRaiserMechanic().getObservable();
    }

    @Override
    default IEventRaiser<? extends ObservablesEventsType> getRaiser() {
        return getObservableRaiserMechanic().getRaiser();
    }

    @Override
    default IObserverHolder getObserverHolder() {
        return getObservableRaiserMechanic().getObserverHolder();
    }

    @Override
    default Class<? extends ObserverType> getObserverType() {
        return getObservableRaiserMechanic().getObserverType();
    }
}
