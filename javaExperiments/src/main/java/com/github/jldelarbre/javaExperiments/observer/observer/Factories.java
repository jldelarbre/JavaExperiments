package com.github.jldelarbre.javaExperiments.observer.observer;

import static com.github.jldelarbre.javaExperiments.observer.observer.internal.Factories.buildEventRaiser;
import static com.github.jldelarbre.javaExperiments.observer.observer.internal.Factories.buildInternalObservableRaiserEngine;
import static com.github.jldelarbre.javaExperiments.observer.observer.internal.Factories.buildObservable;
import static com.github.jldelarbre.javaExperiments.observer.observer.internal.Factories.buildObserverMerger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.jldelarbre.javaExperiments.observer.observer.internal.IObserverMerger;

public final class Factories {
    
    private Factories() {}

    /**
     * Group of {@link IObservableRaiserEngine} that will belong to the same {@link IObservable}.<p>
     * When an event is raised by any of {@link IObservableRaiserEngine} every {@link IObserver}
     * registered in every {@link IObservableRaiserEngine} of this group will be notified.
     */
    public static final class ObservableRaiserEngineGroup {
        private final Map<Class<? extends IEvents>, IObservableRaiserEngine<?, ?>> observableRaiserEngineMap;

        private ObservableRaiserEngineGroup(Map<Class<? extends IEvents>, IObservableRaiserEngine<?, ?>> observableRaiserEngineMap) {
            this.observableRaiserEngineMap = observableRaiserEngineMap;
        }

        public <EventsType extends IEvents, ObserverType extends IObserver<? extends EventsType>>
            IObservableRaiserEngine<EventsType, ObserverType>
                getObservableRaiserEngine(Class<EventsType> eventType) {
            
            @SuppressWarnings("unchecked")
            final IObservableRaiserEngine<EventsType, ObserverType> observableRaiserEngine =
                (IObservableRaiserEngine<EventsType, ObserverType>) this.observableRaiserEngineMap
                        .get(eventType);
            return observableRaiserEngine;
        }
    }

    public static <EventsType extends IEvents, ObserverType extends IObserver<? extends EventsType>>
        IObservableRaiserEngine<EventsType, ObserverType> buildObservableRaiserEngine(Class<EventsType> eventsType) {

        final IObservable<EventsType, ObserverType> observable = buildObservable(eventsType);

        final Set<IObservable<EventsType, ObserverType>> observables = new HashSet<>();
        observables.add(observable);

        final IObserverMerger observerMerger = buildObserverMerger(observables);

        final IEventRaiser<EventsType> raiser = buildEventRaiser(eventsType, observerMerger);

        return buildInternalObservableRaiserEngine(raiser, observable);
    }

    public static ObservableRaiserEngineGroup buildObservableRaiserEngineGroup(Class<? extends IEvents>[] eventsTypes) {

        final Map<Class<? extends IEvents>, IObservableRaiserEngine<?, ?>> observableRaiserEngineMap =
            new HashMap<>();
        final Set<IObservable<?, ?>> observables = new HashSet<>();
        final Map<Class<? extends IEvents>, IObservable<?, ?>> observablesMap = new HashMap<>();

        for (final Class<? extends IEvents> eventsType : eventsTypes) {
            final IObservable<?, ?> observable = buildObservable(eventsType);
            observables.add(observable);
            observablesMap.put(eventsType, observable);
        }

        final IObserverMerger observerMerger = buildObserverMerger(observables);

        for (final Class<? extends IEvents> eventsType : eventsTypes) {
            final IObservableRaiserEngine<?, ?> orm = buildHelper(eventsType, observerMerger, observablesMap);
            observableRaiserEngineMap.put(eventsType, orm);
        }

        return new ObservableRaiserEngineGroup(observableRaiserEngineMap);
    }
    
    private static <EventsType extends IEvents, ObserverType extends IObserver<? extends EventsType>>
        IObservableRaiserEngine<EventsType, ObserverType>
            buildHelper(Class<EventsType> eventsType,
                        IObserverMerger observerMerger,
                        Map<Class<? extends IEvents>, IObservable<?, ?>> observablesMap) {
        
        final IEventRaiser<EventsType> raiser = buildEventRaiser(eventsType, observerMerger);
        @SuppressWarnings("unchecked")
        final IObservable<EventsType, ObserverType> observableTmp =
            (IObservable<EventsType, ObserverType>) observablesMap.get(eventsType);
        
        final IObservableRaiserEngine<EventsType, ObserverType> orm =
            buildInternalObservableRaiserEngine(raiser, observableTmp);
        
        return orm;
    }
}
