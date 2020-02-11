package com.github.jldelarbre.javaExperiments.observer.observer;

public interface IObservableRaiserEngine<EventsType extends IEvents, ObserverType extends IObserver<? extends EventsType>>
        extends IObservable<EventsType, ObserverType>, IEventRaiser<EventsType> {

    interface IProxy<EventsType extends IEvents, ObserverType extends IObserver<? extends EventsType>>
        extends IObservableRaiserEngine<EventsType, ObserverType>,
        IObservable.IProxy<EventsType, ObserverType>, IEventRaiser.IProxy<EventsType> {
        
        IObservableRaiserEngine<EventsType, ObserverType> getObservableRaiserEngine();
        
        @Override
        default IObservable<EventsType, ObserverType> getObservable() {
            return getObservableRaiserEngine();
        }
        
        @Override
        default IEventRaiser<EventsType> getRaiser() {
            return getObservableRaiserEngine();
        }
    }
}
