package com.github.jldelarbre.javaExperiments.observer.observer.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.jldelarbre.javaExperiments.observer.observer.IObservable;
import com.github.jldelarbre.javaExperiments.observer.observer.IObservablesEvents;
import com.github.jldelarbre.javaExperiments.observer.observer.IObserver;

public final class ObserverMerger implements IObserverMerger {

    // pair observable + event
    private final Map<Class<? extends IObservablesEvents>, IObservable<?, ?>> observablesMap;

    private ObserverMerger(Map<Class<? extends IObservablesEvents>, IObservable<?, ?>> observablesMap) {
        this.observablesMap = observablesMap;
    }

    public static ObserverMerger build(Set<? extends IObservable<?, ?>> observables) {
        final Map<Class<? extends IObservablesEvents>, IObservable<?, ?>> observablesMap = new HashMap<>();
        observables.forEach(observable -> {
            final Class<? extends IObservablesEvents> observableClass = observable.getObservablesEventsType();

            observablesMap.put(observableClass, observable);
        });
        return new ObserverMerger(observablesMap);
    }

    @Override
    public Collection<IObserver<?>> getAllObservers() {
        final Set<IObserver<?>> observersOut = new HashSet<>();

        for (final IObservable<?, ?> observable : this.observablesMap.values()) {
            final Set<? extends IObserver<?>> observers = observable.getObservers();
            observersOut.addAll(observers);
        }

        return observersOut;
    }
}
