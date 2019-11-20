package com.github.jldelarbre.javaExperiments.observer.observer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.jldelarbre.javaExperiments.observer.observer.internal.EventRaiser;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.IObserverMerger;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.Observable;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.ObserverMerger;

public final class ObservableRaiserMechanic<ObservablesEventsType extends IObservablesEvents>
        implements IObservableRaiserMechanic<ObservablesEventsType> {

    private final IEventRaiser<ObservablesEventsType> raiser;

    private final IObservable<ObservablesEventsType> observable;

    private ObservableRaiserMechanic(IEventRaiser<ObservablesEventsType> raiser,
                                     IObservable<ObservablesEventsType> observable) {
        this.raiser = raiser;
        this.observable = observable;
    }

    public static class ObservableRaiserMechanicConstruction {
        private final Map<Class<? extends IObservablesEvents>, ObservableRaiserMechanic<?>> observableRaiserMechanicMap;

        private ObservableRaiserMechanicConstruction(Map<Class<? extends IObservablesEvents>, ObservableRaiserMechanic<?>> observableRaiserMechanicMap) {
            this.observableRaiserMechanicMap = observableRaiserMechanicMap;
        }

        public <ObservablesEventsType extends IObservablesEvents> ObservableRaiserMechanic<ObservablesEventsType>
            getObservableRaiserMechanic(Class<ObservablesEventsType> observableEventType) {
            @SuppressWarnings("unchecked")
            final ObservableRaiserMechanic<ObservablesEventsType> observableRaiserMechanic =
                (ObservableRaiserMechanic<ObservablesEventsType>) this.observableRaiserMechanicMap
                        .get(observableEventType);
            return observableRaiserMechanic;
        }
    }

    public static <ObservablesEventsType extends IObservablesEvents> ObservableRaiserMechanic<ObservablesEventsType>
        build(Class<ObservablesEventsType> observablesEventsType) {

        final Observable<ObservablesEventsType> observable = Observable.build(observablesEventsType);

        final Set<IObservable<ObservablesEventsType>> observables = new HashSet<>();
        observables.add(observable);

        final IObserverMerger observerMerger = ObserverMerger.build(observables);

        final IEventRaiser<ObservablesEventsType> raiser = EventRaiser.build(observablesEventsType, observerMerger);

        return new ObservableRaiserMechanic<>(raiser, observable);
    }

    public static ObservableRaiserMechanicConstruction
        build(Class<? extends IObservablesEvents>[] observablesEventsTypes) {

        final Map<Class<? extends IObservablesEvents>, ObservableRaiserMechanic<?>> observableRaiserMechanicMap =
            new HashMap<>();
        final Set<IObservable<? extends IObservablesEvents>> observables = new HashSet<>();
        final Map<Class<? extends IObservablesEvents>, IObservable<? extends IObservablesEvents>> observablesMap =
            new HashMap<>();

        for (final Class<? extends IObservablesEvents> observablesEventsType : observablesEventsTypes) {
            final Observable<? extends IObservablesEvents> observable = Observable.build(observablesEventsType);
            observables.add(observable);
            observablesMap.put(observablesEventsType, observable);
        }

        final IObserverMerger observerMerger = ObserverMerger.build(observables);

        for (final Class<? extends IObservablesEvents> observablesEventsType : observablesEventsTypes) {
            final IEventRaiser<? extends IObservablesEvents> raiser =
                EventRaiser.build(observablesEventsType, observerMerger);
            final IObservable<? extends IObservablesEvents> observableTmp = observablesMap.get(observablesEventsType);
            @SuppressWarnings({ "rawtypes", "unchecked" })
            final ObservableRaiserMechanic<?> orm = new ObservableRaiserMechanic(raiser, observableTmp);
            observableRaiserMechanicMap.put(observablesEventsType, orm);
        }

        return new ObservableRaiserMechanicConstruction(observableRaiserMechanicMap);
    }

    @Override
    public IEventRaiser<ObservablesEventsType> getRaiser() {
        return this.raiser;
    }

    @Override
    public IObservable<ObservablesEventsType> getObservable() {
        return this.observable;
    }

    @Override
    public IObservableRaiserMechanic<ObservablesEventsType> getObservableRaiserMechanic() {
        throw new UnsupportedOperationException("getObservableRaiserMechanic is expected to be used for delegation only");
    }
}
