
package com.github.jldelarbre.javaExperiments.observer.observer;

import com.github.jldelarbre.javaExperiments.observer.observer.internal.IIEventRaiserProxy;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.IObservableProxy;

public interface IObservableRaiserMechanic<ObserverType extends IObserver<ObservablesEventsType>, ObservablesEventsType extends IObservablesEvents>
    extends IObservableProxy<ObserverType, ObservablesEventsType>, IIEventRaiserProxy<ObservablesEventsType> {

    IObservableRaiserMechanic<ObserverType, ObservablesEventsType> getObservableRaiserMechanic();

    @Override
    default IObservable<ObserverType, ObservablesEventsType> getObservable() {
        return getObservableRaiserMechanic().getObservable();
    }

    @Override
    default IEventRaiser<? extends ObservablesEventsType> getRaiser() {
        return getObservableRaiserMechanic().getRaiser();
    }
}
