package com.github.jldelarbre.javaExperiments.observer.observer;

public interface IObservableRaiserMechanic<ObservablesEventsType extends IObservablesEvents>
        extends IObservable<ObservablesEventsType>, IEventRaiser<ObservablesEventsType> {

    interface IProxy<ObservablesEventsType extends IObservablesEvents> extends IObservableRaiserMechanic<ObservablesEventsType>,
        IObservable.IProxy<ObservablesEventsType>, IEventRaiser.IProxy<ObservablesEventsType> {
        
        IObservableRaiserMechanic<ObservablesEventsType> getObservableRaiserMechanic();
        
        @Override
        default IObservable<ObservablesEventsType> getObservable() {
            return getObservableRaiserMechanic();
        }
        
        @Override
        default IEventRaiser<ObservablesEventsType> getRaiser() {
            return getObservableRaiserMechanic();
        }
    }
}
