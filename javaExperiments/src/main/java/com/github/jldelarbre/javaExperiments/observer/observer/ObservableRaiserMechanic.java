package com.github.jldelarbre.javaExperiments.observer.observer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.jldelarbre.javaExperiments.observer.observer.internal.EventRaiser;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.IObserverMerger;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.Observable;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.ObserverMerger;

public final class ObservableRaiserMechanic<EventsType extends IEvents, ObserverType extends IObserver<? extends EventsType>>
        implements IObservableRaiserMechanic<EventsType, ObserverType>,
                   IObservable.IProxy<EventsType, ObserverType>, IEventRaiser.IProxy<EventsType> {

    private final IEventRaiser<EventsType> raiser;

    private final IObservable<EventsType, ObserverType> observable;

    private ObservableRaiserMechanic(IEventRaiser<EventsType> raiser,
                                     IObservable<EventsType, ObserverType> observable) {
        this.raiser = raiser;
        this.observable = observable;
    }

    public static final class ObservableRaiserMechanicConstruction {
        private final Map<Class<? extends IEvents>, ObservableRaiserMechanic<?, ?>> observableRaiserMechanicMap;

        private ObservableRaiserMechanicConstruction(Map<Class<? extends IEvents>, ObservableRaiserMechanic<?, ?>> observableRaiserMechanicMap) {
            this.observableRaiserMechanicMap = observableRaiserMechanicMap;
        }

        public <EventsType extends IEvents, ObserverType extends IObserver<? extends EventsType>>
            ObservableRaiserMechanic<EventsType, ObserverType>
            getObservableRaiserMechanic(Class<EventsType> eventType) {
            
            @SuppressWarnings("unchecked")
            final ObservableRaiserMechanic<EventsType, ObserverType> observableRaiserMechanic =
                (ObservableRaiserMechanic<EventsType, ObserverType>) this.observableRaiserMechanicMap
                        .get(eventType);
            return observableRaiserMechanic;
        }
    }

    public static <EventsType extends IEvents, ObserverType extends IObserver<? extends EventsType>>
        ObservableRaiserMechanic<EventsType, ObserverType> build(Class<EventsType> eventsType) {

        final Observable<EventsType, ObserverType> observable = Observable.build(eventsType);

        final Set<IObservable<EventsType, ObserverType>> observables = new HashSet<>();
        observables.add(observable);

        final IObserverMerger observerMerger = ObserverMerger.build(observables);

        final IEventRaiser<EventsType> raiser = EventRaiser.build(eventsType, observerMerger);

        return new ObservableRaiserMechanic<>(raiser, observable);
    }

    public static ObservableRaiserMechanicConstruction build(Class<? extends IEvents>[] eventsTypes) {

        final Map<Class<? extends IEvents>, ObservableRaiserMechanic<?, ?>> observableRaiserMechanicMap =
            new HashMap<>();
        final Set<IObservable<?, ?>> observables = new HashSet<>();
        final Map<Class<? extends IEvents>, IObservable<?, ?>> observablesMap =
            new HashMap<>();

        for (final Class<? extends IEvents> eventsType : eventsTypes) {
            final Observable<?, ?> observable = Observable.build(eventsType);
            observables.add(observable);
            observablesMap.put(eventsType, observable);
        }

        final IObserverMerger observerMerger = ObserverMerger.build(observables);

        for (final Class<? extends IEvents> eventsType : eventsTypes) {
            final IEventRaiser<? extends IEvents> raiser =
                EventRaiser.build(eventsType, observerMerger);
            final IObservable<?, ?> observableTmp = observablesMap.get(eventsType);
            @SuppressWarnings({ "rawtypes", "unchecked" })
            final ObservableRaiserMechanic<?, ?> orm = new ObservableRaiserMechanic(raiser, observableTmp);
            observableRaiserMechanicMap.put(eventsType, orm);
        }

        return new ObservableRaiserMechanicConstruction(observableRaiserMechanicMap);
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
