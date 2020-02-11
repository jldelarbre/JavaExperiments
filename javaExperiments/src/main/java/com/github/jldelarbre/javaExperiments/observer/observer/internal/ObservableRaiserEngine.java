package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import com.github.jldelarbre.javaExperiments.observer.observer.IEventRaiser;
import com.github.jldelarbre.javaExperiments.observer.observer.IEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservableRaiserEngine;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

final class ObservableRaiserEngine<EventsType extends IEvents, ObserverType extends IObserver<? extends EventsType>>
        implements IObservableRaiserEngine<EventsType, ObserverType>,
                   IObservable.IProxy<EventsType, ObserverType>, IEventRaiser.IProxy<EventsType> {

    private final IEventRaiser<EventsType> raiser;

    private final IObservable<EventsType, ObserverType> observable;

    ObservableRaiserEngine(IEventRaiser<EventsType> raiser, IObservable<EventsType, ObserverType> observable) {
        this.raiser = raiser;
        this.observable = observable;
    }

    @Override
    public IEventRaiser<EventsType> getRaiser() {
        return this.raiser;
    }

    @Override
    public IObservable<EventsType, ObserverType> getObservable() {
        return this.observable;
    }
}
