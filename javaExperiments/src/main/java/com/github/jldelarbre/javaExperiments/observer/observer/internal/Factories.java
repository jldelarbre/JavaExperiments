package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import static com.github.jldelarbre.javaExperiments.observer.observer.internal.EventRaiser.buildRaiser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.jldelarbre.javaExperiments.observer.observer.IEventRaiser;
import com.github.jldelarbre.javaExperiments.observer.observer.IEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservableRaiserEngine;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public final class Factories {

    private Factories() {}

    public static <EventsType extends IEvents, ObserverType extends IObserver<? extends EventsType>>
        IObservableRaiserEngine<EventsType, ObserverType>
            buildInternalObservableRaiserEngine(IEventRaiser<EventsType> raiser,
                                        IObservable<EventsType, ObserverType> observable) {

        return new ObservableRaiserEngine<>(raiser, observable);
    }
    
    public static <EventsType extends IEvents, ObserverType extends IObserver<? extends EventsType>>
        IObservable<EventsType, ObserverType> buildObservable(Class<EventsType> eventsType) {
    
        return new Observable<>(eventsType);
    }
    
    public static <EventsType extends IEvents>
        IEventRaiser<EventsType> buildEventRaiser(Class<EventsType> eventsType, IObserverMerger observerMerger) {

        final EventsType events = buildRaiser(eventsType, observerMerger);
        return new EventRaiser<>(events);
    }
    
    public static IObserverMerger buildObserverMerger(Set<? extends IObservable<?, ?>> observables) {
        final Map<Class<? extends IEvents>, IObservable<?, ?>> observablesMap = new HashMap<>();
        observables.forEach(observable -> {
            final Class<? extends IEvents> observableClass = observable.getObservableEventsType();

            observablesMap.put(observableClass, observable);
        });
        return new ObserverMerger(observablesMap);
    }
}
