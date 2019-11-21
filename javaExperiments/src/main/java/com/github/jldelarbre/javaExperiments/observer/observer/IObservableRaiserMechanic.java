package com.github.jldelarbre.javaExperiments.observer.observer;

public interface IObservableRaiserMechanic<ObservablesEventsType extends IObservablesEvents, ObserverType extends IObserver<? extends ObservablesEventsType>>
        extends IObservable<ObservablesEventsType, ObserverType>, IEventRaiser<ObservablesEventsType> {

    interface IProxy<ObservablesEventsType extends IObservablesEvents, ObserverType extends IObserver<? extends ObservablesEventsType>>
        extends IObservableRaiserMechanic<ObservablesEventsType, ObserverType>,
        IObservable.IProxy<ObservablesEventsType, ObserverType>, IEventRaiser.IProxy<ObservablesEventsType> {
        
        IObservableRaiserMechanic<ObservablesEventsType, ObserverType> getObservableRaiserMechanic();
        
        @Override
        default IObservable<ObservablesEventsType, ObserverType> getObservable() {
            return getObservableRaiserMechanic();
        }
        
        @Override
        default IEventRaiser<ObservablesEventsType> getRaiser() {
            return getObservableRaiserMechanic();
        }
    }
}
