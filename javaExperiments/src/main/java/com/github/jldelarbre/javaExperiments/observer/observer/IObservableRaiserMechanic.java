package com.github.jldelarbre.javaExperiments.observer.observer;

public interface IObservableRaiserMechanic<ObservablesEventsType extends IObservablesEvents>
        extends IObservable.IProxy<ObservablesEventsType>, IEventRaiser.IProxy<ObservablesEventsType> {

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
