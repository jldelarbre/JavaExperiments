package com.github.jldelarbre.javaExperiments.observer.observer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.jldelarbre.javaExperiments.observer.observer.internal.EventRaiser;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.IObserverMerger;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.Observable;
import com.github.jldelarbre.javaExperiments.observer.observer.internal.ObserverMerger;

public final class ObservableRaiserMechanic<ObservablesEventsType extends IObservablesEvents, ObserverType extends IObserver<? extends ObservablesEventsType>>
        implements IObservableRaiserMechanic<ObservablesEventsType, ObserverType>,
        IObservable.IProxy<ObservablesEventsType, ObserverType>, IEventRaiser.IProxy<ObservablesEventsType> {

    private final IEventRaiser<ObservablesEventsType> raiser;

    private final IObservable<ObservablesEventsType, ObserverType> observable;

    private ObservableRaiserMechanic(IEventRaiser<ObservablesEventsType> raiser,
                                     IObservable<ObservablesEventsType, ObserverType> observable) {
        this.raiser = raiser;
        this.observable = observable;
    }

    public static final class ObservableRaiserMechanicConstruction {
        private final Map<Class<? extends IObservablesEvents>, ObservableRaiserMechanic<?, ?>> observableRaiserMechanicMap;

        private ObservableRaiserMechanicConstruction(Map<Class<? extends IObservablesEvents>, ObservableRaiserMechanic<?, ?>> observableRaiserMechanicMap) {
            this.observableRaiserMechanicMap = observableRaiserMechanicMap;
        }

        public <ObservablesEventsType extends IObservablesEvents, ObserverType extends IObserver<? extends ObservablesEventsType>>
            ObservableRaiserMechanic<ObservablesEventsType, ObserverType>
            getObservableRaiserMechanic(Class<ObservablesEventsType> observableEventType) {
            
            @SuppressWarnings("unchecked")
            final ObservableRaiserMechanic<ObservablesEventsType, ObserverType> observableRaiserMechanic =
                (ObservableRaiserMechanic<ObservablesEventsType, ObserverType>) this.observableRaiserMechanicMap
                        .get(observableEventType);
            return observableRaiserMechanic;
        }
    }

    public static <ObservablesEventsType extends IObservablesEvents, ObserverType extends IObserver<? extends ObservablesEventsType>>
        ObservableRaiserMechanic<ObservablesEventsType, ObserverType>
        build(Class<ObservablesEventsType> observablesEventsType) {

        final Observable<ObservablesEventsType, ObserverType> observable = Observable.build(observablesEventsType);

        final Set<IObservable<ObservablesEventsType, ObserverType>> observables = new HashSet<>();
        observables.add(observable);

        final IObserverMerger observerMerger = ObserverMerger.build(observables);

        final IEventRaiser<ObservablesEventsType> raiser = EventRaiser.build(observablesEventsType, observerMerger);

        return new ObservableRaiserMechanic<>(raiser, observable);
    }

    public static ObservableRaiserMechanicConstruction
        build(Class<? extends IObservablesEvents>[] observablesEventsTypes) {

        final Map<Class<? extends IObservablesEvents>, ObservableRaiserMechanic<?, ?>> observableRaiserMechanicMap =
            new HashMap<>();
        final Set<IObservable<?, ?>> observables = new HashSet<>();
        final Map<Class<? extends IObservablesEvents>, IObservable<?, ?>> observablesMap =
            new HashMap<>();

        for (final Class<? extends IObservablesEvents> observablesEventsType : observablesEventsTypes) {
            final Observable<?, ?> observable = Observable.build(observablesEventsType);
            observables.add(observable);
            observablesMap.put(observablesEventsType, observable);
        }

        final IObserverMerger observerMerger = ObserverMerger.build(observables);

        for (final Class<? extends IObservablesEvents> observablesEventsType : observablesEventsTypes) {
            final IEventRaiser<? extends IObservablesEvents> raiser =
                EventRaiser.build(observablesEventsType, observerMerger);
            final IObservable<?, ?> observableTmp = observablesMap.get(observablesEventsType);
            @SuppressWarnings({ "rawtypes", "unchecked" })
            final ObservableRaiserMechanic<?, ?> orm = new ObservableRaiserMechanic(raiser, observableTmp);
            observableRaiserMechanicMap.put(observablesEventsType, orm);
        }

        return new ObservableRaiserMechanicConstruction(observableRaiserMechanicMap);
    }

    @Override
    public IEventRaiser<ObservablesEventsType> getRaiser() {
        return this.raiser;
    }

    @Override
    public IObservable<ObservablesEventsType, ObserverType> getObservable() {
        return this.observable;
    }
}
