package com.github.jldelarbre.javaExperiments.observer.observer;

public interface IObservableRaiserMechanic<EventsType extends IEvents, ObserverType extends IObserver<? extends EventsType>>
        extends IObservable<EventsType, ObserverType>, IEventRaiser<EventsType> {

    interface IProxy<EventsType extends IEvents, ObserverType extends IObserver<? extends EventsType>>
        extends IObservableRaiserMechanic<EventsType, ObserverType>,
        IObservable.IProxy<EventsType, ObserverType>, IEventRaiser.IProxy<EventsType> {
        
        IObservableRaiserMechanic<EventsType, ObserverType> getObservableRaiserMechanic();
        
        @Override
        default IObservable<EventsType, ObserverType> getObservable() {
            return getObservableRaiserMechanic();
        }
        
        @Override
        default IEventRaiser<EventsType> getRaiser() {
            return getObservableRaiserMechanic();
        }
    }
}
